<%@ page import="springonerater.Speaker" %>



<div class="fieldcontain ${hasErrors(bean: speakerInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="speaker.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${speakerInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: speakerInstance, field: 'sessionName', 'error')} ">
	<label for="sessionName">
		<g:message code="speaker.sessionName.label" default="Session Name" />
		
	</label>
	<g:textField name="sessionName" value="${speakerInstance?.sessionName}"/>
</div>

