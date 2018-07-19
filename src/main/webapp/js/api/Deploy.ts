/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
/*部署模块ts模版代码*/
module api {
    export class Deploy  {

        add: string = "add";
        view: string = "view";
        edit: string = "edit";

        /**
         * 环境集-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        public exportAddDeploySetContext(type: string = this.add || this.edit || this.view , biz?: string, data?: any){
           return `
           <p>环境集</p>
            
            `
        }

        /**
         * 服务-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        public exportAddDeployServiceContext(type: string = this.add || this.edit || this.view , biz?: string, data?: any){
            return `
           <p>服务</p>
            
            `
        }

        /**
         * 节点-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        public exportAddDeployHostContext(type: string = this.add || this.edit || this.view , biz?: string, data?: any){

            return `
           <p>节点</p>
            
            `
        }

        /**
         * 部署单元-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        public exportAddDeployUnitContext(type: string = this.add || this.edit || this.view , biz?: string, data?: any){

            return `
           <p>部署单元</p>
            
            `
        }


        /**
         * 环境集操作栏
         * @param value
         * @param row
         */
        public exportDeploySetActionContext(value, row){
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewDeploySetEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewDeploySetEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delDeploySet(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`
        }

        /**
         * 节点操作栏
         * @param value
         * @param row
         */
        public exportDeployHostActionContext(value, row){
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewDeployHostOrEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewDeployHostOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delDeployHost(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`
        }

        /**
         * 服务操作栏
         * @param value
         * @param row
         */
        public exportDeployServiceActionContext(value, row){
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewDeployServiceOrEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewDeployServiceOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delDeployService(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`
        }

        /**
         * 部署单元操作栏
         * @param value
         * @param row
         */
        public exportDeployUnitActionContext(value, row){
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewDeployUnitOrEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewDeployUnitOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delDeployUnit(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`
        }
    }
}