package org.evosuite.coverage.epa;

import org.evosuite.testcase.execution.ExecutionTrace;
import org.evosuite.testcase.execution.MethodCall;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EPATrace {
	private List<EPATransition> epaTransitions;

	public EPATrace(ExecutionTrace executionTrace, EPA epa) {
		epaTransitions = new ArrayList<>();
		EPAState originState = epa.getInitialState();
		for (MethodCall methodCallexecution : executionTrace.getMethodCalls()) {
			String actionName = getCleanMethodName(methodCallexecution.methodName);
			if (actionName.equals("<init>"))
				actionName = epa.getName();
			//final EPAState destinationState = epa.getStateByName(methodCallexecution.endState);
			// TODO: Remove this once MethodCall.endState is implemented
			final EPAState destinationState = epa.temp_anyPossibleDestinationState(originState, actionName);
			if (destinationState == null)
				return;
			
			epaTransitions.add(new EPATransition(originState, actionName, destinationState));
			originState = destinationState;
		}
	}
	private String getCleanMethodName(final String methodName) {
		String[] splitted = methodName.split("\\(");
		return splitted[0];
	}

	public List<EPATransition> getEpaTransitions() {
		return epaTransitions;
	}
}
