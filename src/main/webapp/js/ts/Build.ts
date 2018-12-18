/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
/// <reference path="./Mapper.ts"/>
/// <reference path="../../plugins/init.js"/>

module api {
    export class Build {

        public buildTasksContext(tasks: any) {
            let html = "";
            for (let i in tasks) {
                let task = tasks[i];
                let dependsHtml = "";
                for (let n in task.depends) {
                    let depend = task.depends[n];
                    dependsHtml += `
                    <li>
                        <span>${depend.serviceName}</span> =>
                        <span>${depend.branchName}</span>
                    </li>
                    `
                }
                html += `
                <tr>
                            <th scope="row">${++i}</th>
                            <td>${task.setName}</td>
                            <td>${task.hostName}</td>
                            <td ><a href="javascript:void(0)" onclick="getTaskBuildList(${task.id})">${task.serviceName}</a></td>
                            <td>${task.deployHostName}</td>
                            <td>
                                <div class="advance-format-item">
                                    <p class="advance-format-title"
                                       onclick="toggleBlock(this)">${task.branch}</p>
                                    <div class="advance-format-content">
                                        <ul>
                                               ${dependsHtml}
                                        </ul>
                                    </div>
                                </div>
                            </td>
                            <td>${task.updatedAt}</td>
                            <td><a href="javascript:void(0)" title="开始构建"
                                   onclick="execBuildService(${task.id})"><i
                                    class="fa fa-play-circle" aria-hidden="true"></i></a>
                            </td>
                        </tr>
                `
            }
            return html;
        }

        public buildingListContext(records: any) {
            let items = "";

            let size = 0;
            if (records !== null && records !== undefined) {
                size = records.length
            }
            for (let i in records) {
                items += `
                <a href="${window.basePath}/build/console/${records[i].id}" class="list-group-item">
                    <p>
                    <i class="fa ${(records[i].status == 0 || records[i].status == 1) ? "text-primary fa-meh-o" : records[i].status == 3 ? "text-danger fa-frown-o" : "text-primary fa-smile-o"} " aria-hidden="true"></i>
                    <span class="build-number">#${size--}-${records[i].buildService}</span> <span class="build-date">${records[i].createdAt}</span>
                    </p>
                    ${(records[i].status == 0 || records[i].status == 1) ? `
                    <div class="progress">
                        <div class="progress-bar progress-bar-striped active" role="progressbar"
                             aria-valuenow="40"
                             aria-valuemin="0" aria-valuemax="100" style="width: 40%;">
                            <span class="sr-only">40%</span>
                        </div>
                    </div>
                    ` : ""}
                </a>    
                `;
            }
            return items;
        }
    }
}