/*
 *
 *  Copyright 2015 Robert Winkler
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package io.github.robwin.circuitbreaker;

final public class HalfClosedState extends CircuitBreakerState {

    HalfClosedState(CircuitBreakerStateMachine stateMachine, CircuitBreakerState currentState) {
        super(stateMachine, currentState);
    }

    /**
     * Requests permission to call this circuitBreaker's backend.
     *
     * @return boolean whether a call should be permitted
     */
    @Override
    public boolean isCallPermitted() {
        return true;
    }

    /**
     * Records a backend failure.
     * This must be called if a call to this backend fails
     */
    @Override
    public void recordFailure() {
        numOfFailures.incrementAndGet();
        retryAfter.set(System.currentTimeMillis() + this.waitInterval);
        stateMachine.transitionToOpenState(this);
    }

    /**
     * Records success of a call to this backend.
     * This must be called after a successful call.
     */
    @Override
    public void recordSuccess() {
        stateMachine.transitionToInitialClosedState();
    }

    /**
     * Get the state of the CircuitBreaker
     */
    @Override
    public CircuitBreaker.State getState() {
        return CircuitBreaker.State.HALF_CLOSED;
    }
}
