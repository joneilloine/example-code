var $j=jQuery.noConflict();
var examBuilder;
// this function is for bootstrapping, it is not unit tested
$j( function() {
	examBuilder = new ExamBuilder($j("div.section").eq(0));
});

var UniqueNameGen = {
	counter : 0,
	increment : function(){
		this.counter++;
	},
	name : function(){
		return "name" + this.counter;
	}
}

var BaseObj = Class.create();
BaseObj.prototype = {
	initialize : function() {
	},
	json : function() {
		return Object.toJSON(this);
	},
	setId : function(id){
		if (!(id == null) && (id.length > 0)){
			this.id = id;
		}
	},
	_setNumericalValue : function(propName, value){
		if (!isNaN(value)){
			this[propName] = value;
		}
	}
};

var Exam = Class.create();
Exam.prototype = Object.extend(new BaseObj(), {
	initialize : function(name, timeLimitInMinutes, passingScore, description, id) {
		this.name = name;
		this.description = description;
		this._setNumericalValue("timeLimitInMinutes", timeLimitInMinutes);
		this._setNumericalValue("passingScore", passingScore);
		this.setId(id);
	},
	"class" :"org.terracotta.reference.exam.domain.Exam"
});

var Section = Class.create();
Section.prototype = Object.extend(new BaseObj(), {
	initialize : function(name, description, id) {
		this.name = name;
		this.description = description;
		this.setId(id);
	},
	"class" :"org.terracotta.reference.exam.domain.Section"
});

var Question = Class.create();
Question.prototype = Object.extend(new BaseObj(), {
	initialize : function(questionNumber, htmlText, pointValue, isRandomOrderChoices, id) {
		this.questionNumber = questionNumber;
		this.htmlText = htmlText;
		this.isRandomOrderChoices = isRandomOrderChoices;
		this._setNumericalValue("pointValue", pointValue);
		this.setId(id);
	},
	"class" :"org.terracotta.reference.exam.domain.Question"
});

var Choice = Class.create();
Choice.prototype = Object.extend(new BaseObj(), {
	initialize : function(text, isCorrect, id) {
		this.text = text;
		this.isCorrectChoice = isCorrect;
		this.setId(id);
	},
	"class" :"org.terracotta.reference.exam.domain.Choice"
});

var AbstractBuilder = Class.create();
AbstractBuilder.prototype = {
	initialize : function(sectionDiv, parent){
		this._init(sectionDiv, parent);
	},
	_init : function(sectionDiv, parent){
		this.sectionDiv = sectionDiv || $j({});
		this.parent = parent;
		this.children = [];
		if (this._hasContent()){
			this._findContent();
		} else {
			this.buildDOM();
		}
		this._setupEventListening();
		this.init();
	},
	_hasContent : function(){
		return ($j(this.sectionDiv).children().size() > 0);
	},
	_findContent : function(){
		this.sectionDiv.children("div.section").each(function(i, nestedSection){
			var foundSectionType = $j(nestedSection).attr("class").split(" ").last();
			this["found" + foundSectionType]($j(nestedSection));
		}.bind(this));		
	},
	_setupEventListening : function(){
		this.sectionDiv.children("input").each(function(i, domElement){
			$j(domElement).click(function(event){
				this["on" + $j(event.target).attr("class")]();
			}.bind(this));
		}.bind(this));
		
		$j("form", this.sectionDiv).submit(function(){
			return this.onSubmit();
		}.bind(this));
	},
	init : function(){},
	buildDOM : function(){},
	onDelete : function(){
		this.sectionDiv.remove();
		if (this.parent){
			this.parent.removeChild(this);
		}
	},
	onSubmit : function(){return false;},
	addChild : function(childBuilder){
		this.children.push(childBuilder);
	},
	removeChild : function(childBuilder){
		this.children = this.children.without(childBuilder);
		this.onChildRemoved(childBuilder);
	},
	onChildRemoved : function(childBuilder){},
	buildExam : function(){},
	_loadExamChildComponents : function(examComponent, arrayName) {
		if (this.children.length > 0){
			examComponent[arrayName] = [];
		}
		this.children.each(function(childBuilder){
			examComponent[arrayName].push(childBuilder.buildExam());
		});
	},
	createSectionDiv : function(divClass){
		return $j( "<div/>").appendTo(this.sectionDiv).addClass("section").addClass(divClass);
	}
};

var ExamBuilder = Class.create();
/*
 * global (static) state
 */
Object.extend(
	ExamBuilder,
	{
		/* questions must be numbered globally for the whole Exam */
		questionNumberCounter : 1,
		resetQuestionNumbering : function(){
			ExamBuilder.questionNumberCounter = 1;
		},
		getAndIncrementQuestionNumber : function(){
			return ExamBuilder.questionNumberCounter++;
		}
	}
);
/*
 * prototype (instance) state
 */
ExamBuilder.prototype = Object.extend(new AbstractBuilder(), {
	init : function() {
		this.form = $j("form", this.sectionDiv);
		this.addChild( new SectionBuilder(this.form.children("div.section"), this, true));
	},
	onSubmit: function() {
		this.setExamJSON(this.buildExam());
		return true;
	},
	buildExam : function() {
		ExamBuilder.resetQuestionNumbering();
		examName = $j("input#name", this.form).val();
		timeLimit = $j("input#timeLimitInMins", this.form).val();
		passingScore = $j("input#passingScore", this.form).val();
		examDesc = $j("input#description", this.form).val();
		examID = $j("input#id", this.form).val();

		var exam = new Exam(examName, timeLimit, passingScore, examDesc, examID);
		
		// at most one child section
		$j(this.children).each(function(){
			exam.section = this.buildExam();
		});
		
		return exam;
	},
	setExamJSON : function(exam) {
		$j("input#jsonExam", this.form).val(exam.json());
	}
});

var SectionBuilder = Class.create();
SectionBuilder.prototype = Object.extend(new AbstractBuilder(), {
	initialize : function(sectionDiv, parent, isTopLevel){
		this.isTopLevel = isTopLevel;
		this._init(sectionDiv, parent);
	},
	buildDOM : function(){
		if (!this.isTopLevel){
			$j( "<span>Name:</span>").appendTo(this.sectionDiv);
			$j( "<input type=\"text\" />").appendTo(this.sectionDiv).attr( {"class":"name", size:"30"});
			$j( "<span>Desc:</span>").appendTo(this.sectionDiv);
			$j( "<input type=\"text\" />").appendTo(this.sectionDiv).attr( {"class":"description", size:"30"});
		}
		$j( "<input type=\"button\" />").appendTo(this.sectionDiv).attr( {"class":"NewNestedSection", value:"New Section"});
		$j( "<input type=\"button\" />").appendTo(this.sectionDiv).attr( {"class":"NewQuestion", value:"New Question"});
		if (!this.isTopLevel){
			$j( "<input type=\"button\" />").appendTo(this.sectionDiv).attr( {"class":"Delete", value:"Delete"});
		}
	},
	buildExam : function() {
		var section = new Section(
				this.sectionDiv.children("input.name").val(), 
				this.sectionDiv.children("input.description").val(), 
				this.sectionDiv.children("input.id").val()
				);
		this._loadExamChildComponents(section, this.childrenArrayName);
		return section;
	},
	onNewNestedSection : function(){
		newDiv = this.createSectionDiv("NestedSection");
		this._nestedSection(newDiv);
	},
	onNewQuestion : function(){
		newDiv = this.createSectionDiv("Question");
		this._question(newDiv);
	},
	foundNestedSection : function(nestedSectionDiv){
		this._nestedSection(nestedSectionDiv);
	},
	foundQuestion : function(nestedSectionDiv){
		this._question(nestedSectionDiv);
	},
	_nestedSection : function(nestedDiv){
		this.addChild( new SectionBuilder(nestedDiv, this));
		this.hideInput(this.sectionDiv.children("input.NewQuestion"));
		this.childrenArrayName = "sections";
	},
	_question : function(nestedDiv){
		this.addChild( new QuestionBuilder(nestedDiv, this));
		this.hideInput(this.sectionDiv.children("input.NewNestedSection"));
		this.childrenArrayName = "questions";
	},
	onChildRemoved : function(childBuilder){
		if (this.children.length == 0){
			this.showInput(this.sectionDiv.children("input.NewQuestion"));
			this.showInput(this.sectionDiv.children("input.NewNestedSection"));
		}
	},
	hideInput : function(input){
		input.attr("disabled","true");
	},
	showInput : function(input){
		input.removeAttr("disabled");
	}
});

var QuestionBuilder = Class.create();
QuestionBuilder.prototype = Object.extend(new AbstractBuilder(), {
	init : function(){
		UniqueNameGen.increment();
		this.name = UniqueNameGen.name();
	},
	buildDOM : function(){
		$j( "<span>Question html:</span>").appendTo(this.sectionDiv);
		$j( "<textarea />").appendTo(this.sectionDiv).attr( {"class":"question", "rows":"3", "cols":"70"});
		$j( "<span>Points:</span>").appendTo(this.sectionDiv);
		$j( "<input type=\"text\" />").appendTo(this.sectionDiv).attr( {"class":"pointValue", size:"10"});
		$j( "<span>Randomize choices:</span>").appendTo(this.sectionDiv);
		$j( "<input type=\"checkbox\" />").appendTo(this.sectionDiv).addClass("Randomize");
		$j( "<input type=\"button\" />").appendTo(this.sectionDiv).attr( {"class":"NewChoice", value:"New Choice"});
		$j( "<input type=\"button\" />").appendTo(this.sectionDiv).attr( {"class":"Delete", value:"Delete"});
	},
	buildExam : function() {
		var question = new Question(
				ExamBuilder.getAndIncrementQuestionNumber(),
				this.sectionDiv.children("textarea.question").val(), 
				this.sectionDiv.children("input.pointValue").val(), 
				this.isRandomized(),
				this.sectionDiv.children("input.id").val()
				);
		this._loadExamChildComponents(question, "choices");
		return question;
	},
	isRandomized : function(){
		return (this.sectionDiv.children("input.Randomize:checked").size() == 1 );
	},
	onNewChoice : function(){
		newDiv = this.createSectionDiv("Choice");
		this.addChild( new ChoiceBuilder(newDiv, this));
	},
	foundChoice : function(nestedSectionDiv){
		this.addChild( new ChoiceBuilder(nestedSectionDiv, this));
	}
});

var ChoiceBuilder = Class.create();
ChoiceBuilder.prototype = Object.extend(new AbstractBuilder(), {
	buildDOM : function(){
		$j( "<span>Choice text:</span>").appendTo(this.sectionDiv);
		$j( "<input type=\"text\" />").appendTo(this.sectionDiv).attr( {"class":"choice", size:"55"});
		$j( "<span>is correct choice:</span>").appendTo(this.sectionDiv);
		$j( "<input type=\"radio\" />").appendTo(this.sectionDiv).attr( {"name":this.radioButtonName(), "class":"CorrectChoice"});
		$j( "<input type=\"button\" />").appendTo(this.sectionDiv).attr( {"class":"Delete", value:"Delete"});
	},
	buildExam : function(){
		return new Choice(
				this.sectionDiv.children("input.choice").val(), 
				this.isCorrectChoice(),
				this.sectionDiv.children("input.id").val()
				);
	},
	onCorrectChoice : function(){
		//nothing to do here
	},
	isCorrectChoice : function(){
		return (this.sectionDiv.children("input.CorrectChoice:checked").size() == 1 );
	},
	radioButtonName : function(){
		if (this.parent == null){
			return "";
		}
		return this.parent.name;
	}
});
