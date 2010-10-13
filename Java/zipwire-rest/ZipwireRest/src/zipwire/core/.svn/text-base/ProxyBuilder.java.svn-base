package zipwire.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import zipwire.exception.ZipwireException;

public class ProxyBuilder<T> {
	private InvocationHandler handler;
	private Class<?> interfaceToUse;
	public ProxyBuilder(InvocationHandler handler, Class<?> interfaceToUse) {
		this.handler = handler;
		this.interfaceToUse = interfaceToUse;
	}
	public T build() {
		try {
			Class proxyClass = Proxy.getProxyClass(interfaceToUse.getClassLoader(), new Class[] { interfaceToUse });
			Constructor constructor = proxyClass.getConstructor(new Class[] { InvocationHandler.class });
			return (T)constructor.newInstance(new Object[] {handler});
		}
		catch(Exception e){
			throw new ZipwireException (e);
		}
	}
}
