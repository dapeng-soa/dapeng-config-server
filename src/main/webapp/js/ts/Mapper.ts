/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
module api {
    export class ServiceInfoRepose {
        ip: string;
        serviceName: string;
        socketId: string;
        status: boolean;
        time: Number;
        tag: string;
    }

    export class CmdRequest {
        sourceClientId: string;
        ip: string;
        containerId: string;
        width: string;
        height: string;
        data: string;
    }
}