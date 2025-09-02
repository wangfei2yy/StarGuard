// 权限管理模块
const PermissionManager = (function() {
    const API_BASE_URL = '/star-guard/api/permission';

    // 授予权限
    function grantPermission(requestData) {
        return $.ajax({
            url: `${API_BASE_URL}/grant`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(requestData)
        });
    }

    // 撤销权限
    function revokePermission(requestData) {
        return $.ajax({
            url: `${API_BASE_URL}/revoke`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(requestData)
        });
    }

    // 获取授予权限表单数据
    function getGrantFormData() {
        const username = $('#grant-username').val();
        const host = $('#grant-host').val();
        const permissionTypes = [];
        const scopeType = $('#grant-scope-type').val();
        const databaseName = $('#grant-database').val();
        const allDatabases = $('#grant-all-databases').is(':checked');
        const withGrantOption = $('#grant-with-grant-option').is(':checked');

        // 收集选中的权限类型
        if ($('#grant-alter').is(':checked')) permissionTypes.push('ALTER');
        if ($('#grant-drop').is(':checked')) permissionTypes.push('DROP');
        if ($('#grant-create-table').is(':checked')) permissionTypes.push('CREATE TABLE');
        if ($('#grant-create-view').is(':checked')) permissionTypes.push('CREATE VIEW');
        if ($('#grant-create-function').is(':checked')) permissionTypes.push('CREATE FUNCTION');
        if ($('#grant-create-mv').is(':checked')) permissionTypes.push('CREATE MATERIALIZED VIEW');
        if ($('#grant-refresh-mv').is(':checked')) permissionTypes.push('REFRESH MATERIALIZED VIEW');
        if ($('#grant-alter-mv').is(':checked')) permissionTypes.push('ALTER MATERIALIZED VIEW');
        if ($('#grant-drop-mv').is(':checked')) permissionTypes.push('DROP MATERIALIZED VIEW');
        if ($('#grant-select-mv').is(':checked')) permissionTypes.push('SELECT MATERIALIZED VIEW');
        if ($('#grant-select').is(':checked')) permissionTypes.push('SELECT');
        if ($('#grant-all').is(':checked')) permissionTypes.push('ALL PRIVILEGES');
        
        // 收集SYSTEM权限类型
        if ($('#grant-create-resource-group').is(':checked')) permissionTypes.push('CREATE RESOURCE GROUP');
        if ($('#grant-create-resource').is(':checked')) permissionTypes.push('CREATE RESOURCE');
        if ($('#grant-create-external-catalog').is(':checked')) permissionTypes.push('CREATE EXTERNAL CATALOG');
        if ($('#grant-repository').is(':checked')) permissionTypes.push('REPOSITORY');
        if ($('#grant-blacklist').is(':checked')) permissionTypes.push('BLACKLIST');
        if ($('#grant-file').is(':checked')) permissionTypes.push('FILE');
        if ($('#grant-operate').is(':checked')) permissionTypes.push('OPERATE');
        if ($('#grant-create-storage-volume').is(':checked')) permissionTypes.push('CREATE STORAGE VOLUME');
        if ($('#grant-security').is(':checked')) permissionTypes.push('SECURITY');

        const requestData = {
            username: username,
            host: host,
            permissionTypes: permissionTypes,
            scopeType: scopeType,
            withGrantOption: withGrantOption
        };

        if (scopeType === 'DATABASE') {
            requestData.databaseName = databaseName;
            requestData.allDatabases = allDatabases;
        } else if (scopeType === 'TABLE') {
            const tableScope = $('#grant-table-scope').val();
            requestData.tableScope = tableScope;
            
            if (tableScope === 'SINGLE_TABLE') {
                requestData.tableName = $('#grant-table-name').val();
                requestData.databaseName = $('#grant-table-database').val();
            } else if (tableScope === 'ALL_TABLES_IN_DATABASE') {
                requestData.databaseName = $('#grant-table-database').val();
            }
            // ALL_TABLES_IN_ALL_DATABASES 不需要额外参数
        } else if (scopeType === 'VIEW') {
            const viewScope = $('#grant-view-scope').val();
            requestData.viewScope = viewScope;
            
            if (viewScope === 'SINGLE_VIEW') {
                requestData.viewName = $('#grant-view-name').val();
                requestData.databaseName = $('#grant-view-database').val();
            } else if (viewScope === 'ALL_VIEWS_IN_DATABASE') {
                requestData.databaseName = $('#grant-view-database').val();
            }
        } else if (scopeType === 'MATERIALIZED_VIEW') {
            const viewScope = $('#grant-view-scope').val();
            requestData.viewScope = viewScope;
            
            if (viewScope === 'SINGLE_VIEW') {
                requestData.viewName = $('#grant-view-name').val();
                requestData.databaseName = $('#grant-view-database').val();
            } else if (viewScope === 'ALL_VIEWS_IN_DATABASE') {
                requestData.databaseName = $('#grant-view-database').val();
            }
        }

        return requestData;
    }

    // 获取撤销权限表单数据
    function getRevokeFormData() {
        const username = $('#revoke-username').val();
        const host = $('#revoke-host').val();
        const permissionTypes = [];
        const scopeType = $('#revoke-scope-type').val();
        const databaseName = $('#revoke-database').val();
        const allDatabases = $('#revoke-all-databases').is(':checked');

        // 收集选中的权限类型
        if ($('#revoke-alter').is(':checked')) permissionTypes.push('ALTER');
        if ($('#revoke-drop').is(':checked')) permissionTypes.push('DROP');
        if ($('#revoke-create-table').is(':checked')) permissionTypes.push('CREATE TABLE');
        if ($('#revoke-create-view').is(':checked')) permissionTypes.push('CREATE VIEW');
        if ($('#revoke-create-function').is(':checked')) permissionTypes.push('CREATE FUNCTION');
        if ($('#revoke-create-mv').is(':checked')) permissionTypes.push('CREATE MATERIALIZED VIEW');
        if ($('#revoke-refresh-mv').is(':checked')) permissionTypes.push('REFRESH MATERIALIZED VIEW');
        if ($('#revoke-alter-mv').is(':checked')) permissionTypes.push('ALTER MATERIALIZED VIEW');
        if ($('#revoke-drop-mv').is(':checked')) permissionTypes.push('DROP MATERIALIZED VIEW');
        if ($('#revoke-select-mv').is(':checked')) permissionTypes.push('SELECT MATERIALIZED VIEW');
        if ($('#revoke-select').is(':checked')) permissionTypes.push('SELECT');
        if ($('#revoke-all').is(':checked')) permissionTypes.push('ALL PRIVILEGES');
        
        // 收集SYSTEM权限类型
        if ($('#revoke-create-resource-group').is(':checked')) permissionTypes.push('CREATE RESOURCE GROUP');
        if ($('#revoke-create-resource').is(':checked')) permissionTypes.push('CREATE RESOURCE');
        if ($('#revoke-create-external-catalog').is(':checked')) permissionTypes.push('CREATE EXTERNAL CATALOG');
        if ($('#revoke-repository').is(':checked')) permissionTypes.push('REPOSITORY');
        if ($('#revoke-blacklist').is(':checked')) permissionTypes.push('BLACKLIST');
        if ($('#revoke-file').is(':checked')) permissionTypes.push('FILE');
        if ($('#revoke-operate').is(':checked')) permissionTypes.push('OPERATE');
        if ($('#revoke-create-storage-volume').is(':checked')) permissionTypes.push('CREATE STORAGE VOLUME');
        if ($('#revoke-security').is(':checked')) permissionTypes.push('SECURITY');

        const requestData = {
            username: username,
            host: host,
            permissionTypes: permissionTypes,
            scopeType: scopeType
        };

        if (scopeType === 'DATABASE') {
            requestData.databaseName = databaseName;
            requestData.allDatabases = allDatabases;
        } else if (scopeType === 'TABLE') {
            const tableScope = $('#revoke-table-scope').val();
            requestData.tableScope = tableScope;
            
            if (tableScope === 'SINGLE_TABLE') {
                requestData.tableName = $('#revoke-table-name').val();
                requestData.databaseName = $('#revoke-table-database').val();
            } else if (tableScope === 'ALL_TABLES_IN_DATABASE') {
                requestData.databaseName = $('#revoke-table-database').val();
            }
            // ALL_TABLES_IN_ALL_DATABASES 不需要额外参数
        } else if (scopeType === 'VIEW') {
            const viewScope = $('#revoke-view-scope').val();
            requestData.viewScope = viewScope;
            
            if (viewScope === 'SINGLE_VIEW') {
                requestData.viewName = $('#revoke-view-name').val();
                requestData.databaseName = $('#revoke-view-database').val();
            } else if (viewScope === 'ALL_VIEWS_IN_DATABASE') {
                requestData.databaseName = $('#revoke-view-database').val();
            }
        } else if (scopeType === 'MATERIALIZED_VIEW') {
            const viewScope = $('#revoke-view-scope').val();
            requestData.viewScope = viewScope;
            
            if (viewScope === 'SINGLE_VIEW') {
                requestData.viewName = $('#revoke-view-name').val();
                requestData.databaseName = $('#revoke-view-database').val();
            } else if (viewScope === 'ALL_VIEWS_IN_DATABASE') {
                requestData.databaseName = $('#revoke-view-database').val();
            }
        }

        return requestData;
    }

    // 验证权限类型是否选择
    function validatePermissionTypes(permissionTypes) {
        return permissionTypes.length > 0;
    }

    // 清空授予权限表单
    function resetGrantForm() {
        $('#grant-permission-form')[0].reset();
    }

    // 清空撤销权限表单
    function resetRevokeForm() {
        $('#revoke-permission-form')[0].reset();
    }

    return {
        grantPermission,
        revokePermission,
        getGrantFormData,
        getRevokeFormData,
        validatePermissionTypes,
        resetGrantForm,
        resetRevokeForm
    };
})();