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
package org.ow2.proactive_grid_cloud_portal.rm.client.monitoring.views;

import java.util.ArrayList;
import java.util.List;

import org.ow2.proactive_grid_cloud_portal.common.client.json.JSONUtils;
import org.ow2.proactive_grid_cloud_portal.common.client.model.LogModel;
import org.ow2.proactive_grid_cloud_portal.common.client.model.LoginModel;
import org.ow2.proactive_grid_cloud_portal.rm.client.RMController;
import org.ow2.proactive_grid_cloud_portal.rm.client.RMModel;
import org.ow2.proactive_grid_cloud_portal.rm.client.RMServiceAsync;
import org.ow2.proactive_grid_cloud_portal.rm.client.monitoring.Reloadable;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;


/**
 * Processes tab in host monitoring.
 */
public class ProcessesView extends VLayout implements Reloadable {
    private ListGrid processesGrid = new ListGrid();

    private RMController controller;

    private String url;

    public ProcessesView(RMController controller, String url) {

        this.controller = controller;
        this.url = url;

        ListGridField pid = new ListGridField("pid", "pid");
        pid.setType(ListGridFieldType.INTEGER);
        ListGridField owner = new ListGridField("owner", "owner");
        ListGridField startTime = new ListGridField("startTime", "Dime");
        ListGridField memSize = new ListGridField("memSize", "Memory");
        ListGridField memRss = new ListGridField("memRss", "Res Memory");
        ListGridField memShare = new ListGridField("memShare", "Share Memory");
        ListGridField cpuTime = new ListGridField("cpuTime", "Cpu Time");
        ListGridField state = new ListGridField("state", "state");
        ListGridField description = new ListGridField("description", "description");
        ListGridField commandline = new ListGridField("commandline", "Command line");
        processesGrid.setFields(pid,
                                owner,
                                startTime,
                                memSize,
                                memRss,
                                memShare,
                                cpuTime,
                                state,
                                description,
                                commandline);

        setWidth100();
        processesGrid.setCanDragSelectText(true);
        addMember(processesGrid);
        load();
    }

    public void load() {

        final List<String> attrs = new ArrayList<String>();
        attrs.add("Processes");

        final RMServiceAsync rm = controller.getRMService();
        final RMModel model = controller.getModel();
        final long t = System.currentTimeMillis();

        final LoginModel loginModel = LoginModel.getInstance();

        // loading runtime info
        rm.getNodeMBeanInfo(loginModel.getSessionId(), url, "sigar:Type=Processes", attrs, new AsyncCallback<String>() {
            public void onSuccess(String result) {
                if (!loginModel.isLoggedIn())
                    return;

                LogModel.getInstance().logMessage("Fetched Runtime info in " + (System.currentTimeMillis() - t) + "ms");

                //[{"name":"Processes","value":[{"startTime":"Dec8","memSize":"4.0M","commandline":["/sbin/init","--arg"],"memRss":"848K","description":"/sbin/init","memShare":"620K","owner":"root","state":"S","pid":1,"cpuTime":"0:3"}]}]

                JSONArray processes = controller.parseJSON(result).isArray().get(0).isObject().get("value").isArray();
                if (processes != null) {
                    ListGridRecord[] records = new ListGridRecord[processes.size()];
                    for (int i = 0; i < processes.size(); i++) {
                        records[i] = new ListGridRecord();
                        JSONObject process = processes.get(i).isObject();
                        try {
                            for (String key : process.keySet()) {
                                ListGridField lgf = processesGrid.getField(key);

                                if (lgf == null)
                                    continue;

                                if (lgf.getType() == ListGridFieldType.INTEGER) {
                                    records[i].setAttribute(key, Integer.parseInt(process.get(key).toString()));
                                } else {
                                    records[i].setAttribute(key, process.get(key).toString());
                                }
                            }
                        } catch (RuntimeException ex) {
                            continue;
                        }
                    }
                    processesGrid.setData(records);
                }
            }

            public void onFailure(Throwable caught) {
                if (JSONUtils.getJsonErrorCode(caught) == 401) {
                    LogModel.getInstance().logMessage("You have been disconnected from the server.");
                }
            }
        });

        processesGrid.draw();
    }

    public void reload() {
        processesGrid.setData(new ListGridRecord[0]);
        processesGrid.draw();
        load();
    }

    @Override
    public void onFinish(Runnable callback) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
