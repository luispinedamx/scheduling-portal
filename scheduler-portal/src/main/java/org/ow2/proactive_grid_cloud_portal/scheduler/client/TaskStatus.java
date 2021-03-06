/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.ow2.proactive_grid_cloud_portal.scheduler.client;

/**
 * This class represents every status that a task is able to be in.<br>
 * Each status are best describe below.
 *
 * @author The ProActive Team
 * @since ProActive Scheduling 0.9
 */
public enum TaskStatus implements java.io.Serializable {

    /**
     * The task has just been submitted by the user.
     */
    SUBMITTED("Submitted"),
    /**
     * The task is in the scheduler pending queue.
     */
    PENDING("Pending"),
    /**
     * The task is paused.
     */
    PAUSED("Paused"),
    /**
     * The task is executing.
     */
    RUNNING("Running"),
    /**
     * The task is waiting for restart after an error. (ie:native code != 0 or exception)
     */
    WAITING_ON_ERROR("Faulty..."),
    /**
     * The task is waiting for restart after a failure. (ie:node down)
     */
    WAITING_ON_FAILURE("Failed..."),
    /**
     * The task is failed
     * (only if max execution time has been reached and the node on which it was started is down).
     */
    FAILED("Resource down"),
    /**
     * The task could not be started.<br>
     * It means that the task could not be started due to
     * dependences failure.
     */
    NOT_STARTED("Could not start"),
    /**
     * The task could not be restarted.<br>
     * It means that the task could not be restarted after an error
     * during the previous execution
     */
    NOT_RESTARTED("Could not restart"),
    /**
     * The task has been aborted by an exception on an other task. (job is cancelOnError=true)
     * Can be also in this status if the job is killed while the concerned task was running.
     */
    ABORTED("Aborted"),
    /**
     * The task has finished execution with error code (!=0) or exception.
     */
    FAULTY("Faulty"),
    /**
     * The task has finished execution.
     */
    FINISHED("Finished"),
    /**
     * Skipped due to flow action
     */
    SKIPPED("Skipped"),
    /**
     * The task is suspended after first error and is waiting for a manual restart action.
     */
    IN_ERROR("In-Error");

    /** The name of the current status. */
    private String name;

    /**
     * Implicit constructor of a status.
     *
     * @param name the name of the status.
     */
    TaskStatus(String name) {
        this.name = name;
    }

    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return name;
    }

    public static TaskStatus from(String name) {
        for (TaskStatus taskStatus : values()) {
            if (taskStatus.name.equals(name)) {
                return taskStatus;
            }
        }

        return null;
    }

}
