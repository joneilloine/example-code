// Straight lambda calculus
// let Y = lambda y . (lambda x . y (x x)) (lambda x . y (x x))

// The Y Combinator
var Y = function (y) {
	return function (x) { return x(x) }(function(x) {
		return y(function() {
			return x(x).apply(null, arguments)
		}
	)
	})
}

// Factorial example
var factorial = Y(function (h) {
	return function (n) { 
  		return (n < 2) ? 1 : n * h(n - 1)
	}
})

factorial(5)

// Not that you actually need a Y combinator of course. This is a shorter version the anonymous factorial function:
/*
var factorial = function (n) {
	return (n < 2) ? 1 : n * arguments.callee(n - 1)
}
*/