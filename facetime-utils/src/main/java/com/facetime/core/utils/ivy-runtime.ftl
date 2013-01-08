<?xml version="1.0" encoding="UTF-8"?>
<ivy-module version="1.0" xmlns:m="http://ant.apache.org/ivy/maven">
	<info organisation="${org}"
		module="${name}"
		revision="${rev}"
		status="integration"
		default="true"
	/>
	<configurations>
		<conf name="default" visibility="public" description="runtime dependencies and master artifact can be used with this conf" extends="runtime,master"/>
		<conf name="master" visibility="public" description="contains only the artifact published by this module itself, with no transitive dependencies"/>
		<conf name="compile" visibility="public" description="this is the default scope, used if none is specified. Compile dependencies are available in all classpaths."/>
		<conf name="provided" visibility="public" description="this is much like compile, but indicates you expect the JDK or a container to provide it. It is only available on the compilation classpath, and is not transitive."/>
		<conf name="runtime" visibility="public" description="this scope indicates that the dependency is not required for compilation, but is for execution. It is in the runtime and test classpaths, but not the compile classpath." extends="compile"/>
		<conf name="test" visibility="private" description="this scope indicates that the dependency is not required for normal use of the application, and is only available for the test compilation and execution phases." extends="runtime"/>
		<conf name="system" visibility="public" description="this scope is similar to provided except that you have to provide the JAR which contains it explicitly. The artifact is always available and is not looked up in a repository."/>
		<conf name="sources" visibility="public" description="this configuration contains the source artifact of this module, if any."/>
		<conf name="javadoc" visibility="public" description="this configuration contains the javadoc artifact of this module, if any."/>
		<conf name="optional" visibility="public" description="contains all optional dependencies"/>
	</configurations>
	<publications>
		<artifact name="${name}" type="jar" ext="jar" conf="default"/>
	</publications>
	<dependencies>
		<dependency org="com.sun" name="activation" rev="1.1"></dependency>
		<dependency org="com.sun" name="ejb3-persistence" rev="3.0"></dependency>
		<dependency org="com.sun" name="jstl" rev="1.1.2"></dependency>
		<dependency org="com.sun" name="jta" rev="1.0"></dependency>
		<dependency org="com.sun" name="mail" rev="1.4"></dependency>
		<dependency org="antlr" name="antlr" rev="2.7.6"></dependency>
		<dependency org="org.objectweb" name="asm" rev="2.2.3"></dependency>
		<dependency org="org.objectweb" name="asm-commons" rev="2.2.3"></dependency>
		<dependency org="org.aspectj" name="aspectjrt" rev="1.5"></dependency>
		<dependency org="org.aspectj" name="aspectjweaver" rev="1.5.3"></dependency>
		<dependency org="org.jasig" name="cas-client-core" rev="3.1.9"></dependency>
		<dependency org="net.sf" name="cglib-nodep" rev="2.1_3"></dependency>
		<dependency org="net.sf" name="ehcache" rev="2.2.0"></dependency>
		<dependency org="org.slf4j" name="slf4j-api" rev="1.6.0"></dependency>
		<dependency org="org.slf4j" name="slf4j-nop" rev="1.6.0"></dependency>
		<dependency org="org.apache" name="commons-beanutils" rev="1.8.2"></dependency>
		<dependency org="org.apache" name="commons-collections" rev="3.1"></dependency>
		<dependency org="org.apache" name="commons-dbcp" rev="1.2.1"></dependency>
		<dependency org="org.apache" name="commons-fileupload" rev="1.1.1"></dependency>
		<dependency org="org.apache" name="commons-io" rev="1.2"></dependency>
		<dependency org="org.apache" name="commons-lang" rev="2.1"></dependency>
		<dependency org="org.apache" name="commons-logging" rev="1.0.4"></dependency>
		<dependency org="org.apache" name="commons-pool" rev="1.2"></dependency>
		<dependency org="org.apache" name="commons-codec" rev="1.4"></dependency>
		<dependency org="org.apache" name="commons-httpclient" rev="3.0"></dependency>
		<dependency org="org.apache" name="httpclient" rev="4.0.1"/>
		<dependency org="org.apache" name="httpmime" rev="4.0.1"/>
		<dependency org="org.apache" name="derby" rev="10.2"></dependency>
		<dependency org="org.apache" name="jakarta-oro" rev="2.0.8"></dependency>
		<dependency org="org.apache" name="log4j" rev="1.2.15"></dependency>
		<dependency org="org.apache" name="lucene-core" rev="2.0.0"></dependency>
		<dependency org="org.apache" name="standard" rev="1.1.2"></dependency>
		<dependency org="org.apache" name="struts2-core" rev="2.0.9"></dependency>
		<dependency org="org.apache" name="struts2-spring-plugin" rev="2.0.9"></dependency>
		<dependency org="org.apache" name="tapestry-gdev" rev="1.0"></dependency>
		<dependency org="org.apache" name="velocity" rev="1.5"></dependency>
		<dependency org="org.apache" name="activeio-core" rev="3.1.0"></dependency>
		<dependency org="org.apache" name="activemq" rev="5.2.0"></dependency>
		<dependency org="org.apache" name="xbean-spring" rev="3.4"></dependency>
		<dependency org="org.dom4j" name="dom4j" rev="1.6"></dependency>
		<dependency org="freemarker" name="freemarker" rev="2.3.8"></dependency>
		<dependency org="com.google" name="gwt-servlet" rev="1.5.3"></dependency>
		<dependency org="org.hibernate" name="hibernate-annotations" rev="3.3.0"></dependency>
		<dependency org="org.hibernate" name="hibernate-commons-annotations" rev="3.3.0"></dependency>
		<dependency org="org.hibernate" name="hibernate" rev="3.2.5"></dependency>
		<dependency org="au.id" name="jericho-html" rev="2.5"></dependency>
		<dependency org="com.mysql" name="mysql-connector-java" rev="5.0.7"></dependency>
		<dependency org="ognl" name="ognl" rev="2.6.11"></dependency>
		<dependency org="org.quartz" name="quartz" rev="1.6.0"></dependency>
		<dependency org="org.springframework" name="spring-security-cas-client" rev="2.0.5"></dependency>
		<dependency org="org.springframework" name="spring-security-core-tiger" rev="2.0.5"></dependency>
		<dependency org="org.springframework" name="spring-security-core" rev="2.0.5"></dependency>
		<dependency org="org.springframework" name="spring-webmvc" rev="2.5.6"></dependency>
		<dependency org="org.springframework" name="spring-test" rev="2.5.6"></dependency>
		<dependency org="org.springframework" name="spring" rev="2.5.6"></dependency>
		<dependency org="com.opensymphony" name="xwork" rev="2.0.4"></dependency>
		<dependency org="com.caucho" name="hessian" rev="3.1.6"></dependency>
	</dependencies>	
</ivy-module>
