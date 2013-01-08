<?xml version="1.0" encoding="UTF-8"?>
<ivy-module version="1.0">
	<info organisation="${org}"
		module="${name}"
		revision="${rev}"
		status="integration"
		default="true"
	/>
	<configurations>
		<conf name="default" visibility="public"/>
	</configurations>
	<publications>
		<artifact name="${name}" type="jar" ext="jar" conf="default"/>
		<artifact name="${name}" type="source" ext="jar" conf="default"/>
	</publications>
</ivy-module>
