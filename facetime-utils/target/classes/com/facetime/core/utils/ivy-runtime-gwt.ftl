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
		<artifact name="${name}" type="jar" ext="jar" conf="master"/>
	</publications>
	<dependencies>
		<dependency org="com.sun" name="ejb3-persistence" rev="3.0"></dependency>
		<dependency org="org.hibernate" name="hibernate-annotations" rev="3.3.0"></dependency>
		<dependency org="org.hibernate" name="hibernate-commons-annotations" rev="3.3.0"></dependency>
		
		<dependency org="com.rednels" name="ofcgwt" rev="1.0"></dependency>
		<dependency org="com.google" name="gwt-maps" rev="1.0.4"></dependency>
		<dependency org="com.google" name="gwt-incubator" rev="1.0"></dependency>
		
		<dependency org="net.goldbutton" name="common-bean" rev="latest.integration"/>
	</dependencies>	
</ivy-module>
