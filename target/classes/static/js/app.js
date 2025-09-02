// 主应用模块
const App = (function() {
    // 初始化应用
    function init() {
        setupEventListeners();
        loadInitialData();
    }

    // 设置事件监听器
    function setupEventListeners() {
        // 授予权限表单提交事件
        $('#grant-permission-form').submit(function(event) {
            event.preventDefault();
            handleGrantPermission();
        });

        // 撤销权限表单提交事件
        $('#revoke-permission-form').submit(function(event) {
            event.preventDefault();
            handleRevokePermission();
        });

        // 标签页切换事件
        $('#main-tabs a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            const tabId = e.target.getAttribute('href');
            // 高亮侧边栏对应的链接
            $('.sidebar-menu a').removeClass('active');
            $(`.sidebar-menu a[href="${tabId}"]`).addClass('active');
        });

        // 侧边栏链接点击事件
        $('.sidebar-menu a').click(function(event) {
            event.preventDefault();
            const tabId = $(this).attr('href');
            // 激活对应的标签页
            $(`#main-tabs a[href="${tabId}"]`).tab('show');
            // 高亮侧边栏链接
            $('.sidebar-menu a').removeClass('active');
            $(this).addClass('active');
        });

        // 权限作用范围切换事件
        $('#grant-scope-type, #revoke-scope-type').change(function() {
            handleScopeTypeChange($(this));
        });

        // 视图作用范围切换事件
        $('#grant-view-scope, #revoke-view-scope').change(function() {
            handleViewScopeChange($(this));
        });

        // 表作用范围切换事件
        $('#grant-table-scope, #revoke-table-scope').change(function() {
            handleTableScopeChange($(this));
        });

        // 权限类型动态显示事件监听器
        $('#grant-scope-type, #revoke-scope-type').change(function() {
            handlePermissionTypeDisplay();
        });

        // 表作用范围切换事件监听器
        $('#grant-table-scope, #revoke-table-scope').change(function() {
            // 当表作用范围改变时，也需要调整权限类型的显示
            handlePermissionTypeDisplay();
        });
        // 页面加载时初始化权限类型显示
        handlePermissionTypeDisplay();
    }

    // 加载初始数据
    function loadInitialData() {
        loadUserList();
    }

    // 加载用户列表
    function loadUserList() {
        UserManager.loadUserList()
            .done(function(response) {
                if (response.code === 200) {
                    UserManager.renderUserList(response.data);
                } else {
                    UIUtils.showMessage('错误', response.message, 'danger');
                }
            })
            .fail(function(xhr, status, error) {
                console.error('加载用户列表失败:', error);
                UIUtils.showMessage('错误', '加载用户列表失败: ' + xhr.responseText, 'danger');
                $('#user-list-body').html('<tr><td colspan="5" class="text-center text-danger">加载用户列表失败，请刷新页面重试</td></tr>');
            });
    }

    // 处理授予权限
    function handleGrantPermission() {
        const formData = PermissionManager.getGrantFormData();
        
        // 验证权限类型是否选择
        if (!PermissionManager.validatePermissionTypes(formData.permissionTypes)) {
            UIUtils.showMessage('错误', '请至少选择一种权限类型', 'danger');
            return;
        }

        const submitBtn = $('#grant-submit-btn');
        UIUtils.setButtonLoading(submitBtn, true, '处理中...', '<i class="fas fa-check mr-2"></i>授予权限');

        PermissionManager.grantPermission(formData)
            .done(function(response) {
                if (response.code === 200) {
                    UIUtils.showMessage('成功', '权限授予成功', 'success');
                    PermissionManager.resetGrantForm();
                    loadUserList();
                } else {
                    UIUtils.showMessage('错误', response.message, 'danger');
                }
            })
            .fail(function(xhr, status, error) {
                console.error('授予权限失败:', error);
                UIUtils.showMessage('错误', '授予权限失败: ' + xhr.responseText, 'danger');
            })
            .always(function() {
                UIUtils.setButtonLoading(submitBtn, false, '', '<i class="fas fa-check mr-2"></i>授予权限');
            });
    }

    // 处理权限作用范围切换
    function handleScopeTypeChange(selectElement) {
        const scopeType = selectElement.val();
        const formId = selectElement.attr('id').includes('grant') ? 'grant' : 'revoke';
        
        console.log(`handleScopeTypeChange: scopeType=${scopeType}, formId=${formId}`);
        
        // 隐藏所有表单组并移除required属性
        $(`#${formId}-database-group`).hide();
        $(`#${formId}-database`).removeAttr('required');
        $(`#${formId}-all-databases-group`).hide();
        $(`#${formId}-view-group`).hide();
        $(`#${formId}-view-name-group`).hide();
        $(`#${formId}-view-name`).removeAttr('required');
        $(`#${formId}-view-database-group`).hide();
        $(`#${formId}-view-database`).removeAttr('required');
        $(`#${formId}-table-group`).hide();
        $(`#${formId}-table-name-group`).hide();
        $(`#${formId}-table-name`).removeAttr('required');
        $(`#${formId}-table-database-group`).hide();
        $(`#${formId}-table-database`).removeAttr('required');
        
        if (scopeType === 'DATABASE') {
            $(`#${formId}-database-group`).show();
            $(`#${formId}-database`).attr('required', 'required');
            $(`#${formId}-all-databases-group`).show();
            console.log('显示数据库相关表单组');
        } else if (scopeType === 'TABLE') {
            $(`#${formId}-table-group`).show();
            console.log('显示表作用范围选择组');
            // 触发表作用范围变化以显示相应字段
            $(`#${formId}-table-scope`).trigger('change');
        } else if (scopeType === 'VIEW') {
            $(`#${formId}-view-group`).show();
            console.log('显示视图作用范围选择组');
            // 触发视图作用范围变化以显示相应字段
            $(`#${formId}-view-scope`).trigger('change');
        } else if (scopeType === 'MATERIALIZED_VIEW') {
            $(`#${formId}-view-group`).show();
            console.log('显示物化视图作用范围选择组');
            // 触发视图作用范围变化以显示相应字段
            $(`#${formId}-view-scope`).trigger('change');
        } else if (scopeType === 'SYSTEM') {
            console.log('显示系统权限相关表单组');
            // System权限不需要额外的表单组，只需要选择权限类型
        }
        
        // 检查元素是否存在
        console.log(`元素存在性检查 - #${formId}-table-group:`, $(`#${formId}-table-group`).length > 0);
        console.log(`元素存在性检查 - #${formId}-table-scope:`, $(`#${formId}-table-scope`).length > 0);
    }

    // 处理视图作用范围切换
    function handleViewScopeChange(selectElement) {
        const viewScope = selectElement.val();
        const formId = selectElement.attr('id').includes('grant') ? 'grant' : 'revoke';
        const scopeType = $(`#${formId}-scope-type`).val();
        
        // 隐藏所有视图相关表单组并移除required属性
        $(`#${formId}-view-name-group`).hide();
        $(`#${formId}-view-name`).removeAttr('required');
        $(`#${formId}-view-database-group`).hide();
        $(`#${formId}-view-database`).removeAttr('required');
        
        // 无论是VIEW还是MATERIALIZED_VIEW，都根据viewScope显示相应字段
        if (viewScope === 'SINGLE_VIEW') {
            $(`#${formId}-view-name-group`).show();
            $(`#${formId}-view-name`).attr('required', 'required');
            $(`#${formId}-view-database-group`).show();
            $(`#${formId}-view-database`).attr('required', 'required');
        } else if (viewScope === 'ALL_VIEWS_IN_DATABASE') {
            $(`#${formId}-view-database-group`).show();
            $(`#${formId}-view-database`).attr('required', 'required');
        }
        // ALL_VIEWS_IN_ALL_DATABASES 不需要显示额外字段
    }

    // 处理表作用范围切换
    function handleTableScopeChange(selectElement) {
        const tableScope = selectElement.val();
        const formId = selectElement.attr('id').includes('grant') ? 'grant' : 'revoke';
        
        console.log(`handleTableScopeChange: tableScope=${tableScope}, formId=${formId}`);
        
        // 隐藏所有表相关表单组并移除required属性
        $(`#${formId}-table-name-group`).hide();
        $(`#${formId}-table-name`).removeAttr('required');
        $(`#${formId}-table-database-group`).hide();
        $(`#${formId}-table-database`).removeAttr('required');
        $(`#${formId}-database-group`).hide();
        $(`#${formId}-database`).removeAttr('required');
        $(`#${formId}-all-databases-group`).hide();
        
        if (tableScope === 'SINGLE_TABLE') {
            $(`#${formId}-table-name-group`).show();
            $(`#${formId}-table-name`).attr('required', 'required');
            $(`#${formId}-table-database-group`).show();
            $(`#${formId}-table-database`).attr('required', 'required');
            console.log('显示表名称和表所在数据库输入框');
        } else if (tableScope === 'ALL_TABLES_IN_DATABASE') {
            $(`#${formId}-table-database-group`).show();
            $(`#${formId}-table-database`).attr('required', 'required');
            console.log('显示表所在数据库输入框');
        } else if (tableScope === 'ALL_TABLES_IN_ALL_DATABASES') {
            // 所有数据库中的所有表不需要显示额外字段
            console.log('不显示额外字段 - 所有数据库中的所有表');
        }
        
        // 检查显示状态
        console.log(`表名称输入框显示状态:`, $(`#${formId}-table-name-group`).is(':visible'));
        console.log(`表所在数据库输入框显示状态:`, $(`#${formId}-table-database-group`).is(':visible'));
    }

    // 权限类型动态显示处理函数
    function handlePermissionTypeDisplay() {
        const grantScopeType = $('#grant-scope-type').val();
        const revokeScopeType = $('#revoke-scope-type').val();
        const grantTableScope = $('#grant-table-scope').val();
        const revokeTableScope = $('#revoke-table-scope').val();
        
        // 数据库权限类型（根据用户要求更新为7种）
        const databasePermissions = ['alter', 'drop', 'create-table', 'create-view', 'create-function', 'create-mv', 'all'];
        
        // 视图权限类型（根据用户要求更新为4种）
        const viewPermissions = ['alter', 'drop', 'select', 'all'];
        
        // 物化视图权限类型（根据用户要求更新为5种）
        const materializedViewPermissions = ['select', 'alter', 'refresh', 'drop', 'all'];
        
        // 表权限类型（8种）
        const tablePermissions = ['alter', 'drop', 'select', 'insert', 'export', 'update', 'delete', 'all'];
        
        // 表权限（不包含ALTER和DROP，用于ALL TABLES IN ALL DATABASES和ALL TABLES IN DATABASE情况）
        const tableDataPermissions = ['select', 'insert', 'export', 'update', 'delete', 'all'];
        
        // System权限类型（9种）
        const systemPermissions = ['create-resource-group', 'create-resource', 'create-external-catalog', 'repository', 'blacklist', 'file', 'operate', 'create-storage-volume', 'security'];
        
        // 处理授予权限表单
        if (grantScopeType === 'DATABASE') {
            // 显示所有数据库权限类型
            databasePermissions.forEach(permission => {
                $(`#grant-${permission}`).closest('.form-check').show();
            });
            // 隐藏其他类型的权限
            $(`#grant-alter-mv`).closest('.form-check').hide();
            $(`#grant-drop-mv`).closest('.form-check').hide();
            $(`#grant-refresh-mv`).closest('.form-check').hide();
            $(`#grant-select-mv`).closest('.form-check').hide();
            $(`#grant-select`).closest('.form-check').hide();
            $(`#grant-insert`).closest('.form-check').hide();
            $(`#grant-export`).closest('.form-check').hide();
            $(`#grant-update`).closest('.form-check').hide();
            $(`#grant-delete`).closest('.form-check').hide();
            // 隐藏系统权限
            systemPermissions.forEach(permission => {
                $(`#grant-${permission}`).closest('.form-check').hide();
            });
        } else if (grantScopeType === 'VIEW') {
            // 只显示视图相关的权限类型
            // 先隐藏所有权限
            databasePermissions.forEach(permission => {
                $(`#grant-${permission}`).closest('.form-check').hide();
            });
            $(`#grant-alter-mv`).closest('.form-check').hide();
            $(`#grant-drop-mv`).closest('.form-check').hide();
            $(`#grant-refresh-mv`).closest('.form-check').hide();
            $(`#grant-select-mv`).closest('.form-check').hide();
            $(`#grant-insert`).closest('.form-check').hide();
            $(`#grant-export`).closest('.form-check').hide();
            $(`#grant-update`).closest('.form-check').hide();
            $(`#grant-delete`).closest('.form-check').hide();
            // 隐藏系统权限
            systemPermissions.forEach(permission => {
                $(`#grant-${permission}`).closest('.form-check').hide();
            });
            // 显示视图权限
            viewPermissions.forEach(permission => {
                $(`#grant-${permission}`).closest('.form-check').show();
            });
        } else if (grantScopeType === 'MATERIALIZED_VIEW') {
            // 只显示物化视图相关的权限类型
            // 先隐藏所有权限
            databasePermissions.forEach(permission => {
                $(`#grant-${permission}`).closest('.form-check').hide();
            });
            $(`#grant-insert`).closest('.form-check').hide();
            $(`#grant-export`).closest('.form-check').hide();
            $(`#grant-update`).closest('.form-check').hide();
            $(`#grant-delete`).closest('.form-check').hide();
            // 隐藏系统权限
            systemPermissions.forEach(permission => {
                $(`#grant-${permission}`).closest('.form-check').hide();
            });
            // 显示物化视图权限
            $(`#grant-select-mv`).closest('.form-check').show(); // SELECT MATERIALIZED VIEW
            $(`#grant-alter-mv`).closest('.form-check').show();  // ALTER MATERIALIZED VIEW
            $(`#grant-refresh-mv`).closest('.form-check').show(); // REFRESH MATERIALIZED VIEW
            $(`#grant-drop-mv`).closest('.form-check').show();   // DROP MATERIALIZED VIEW
            $(`#grant-all`).closest('.form-check').show();       // ALL PRIVILEGES
        } else if (grantScopeType === 'SYSTEM') {
            // 只显示系统相关的权限类型 - 先隐藏所有权限
            
            // 隐藏所有权限选择框 - 使用更简单直接的选择器
            $("input[id^='grant-'][type='checkbox']").closest('.form-check').hide();
            
            // 显式隐藏所有非系统权限
            databasePermissions.forEach(permission => {
                $(`#grant-${permission}`).closest('.form-check').hide();
            });
            
            // 隐藏物化视图相关权限
            $(`#grant-alter-mv`).closest('.form-check').hide();
            $(`#grant-drop-mv`).closest('.form-check').hide();
            $(`#grant-refresh-mv`).closest('.form-check').hide();
            $(`#grant-select-mv`).closest('.form-check').hide();
            
            // 隐藏表相关权限
            $(`#grant-select`).closest('.form-check').hide();
            $(`#grant-insert`).closest('.form-check').hide();
            $(`#grant-export`).closest('.form-check').hide();
            $(`#grant-update`).closest('.form-check').hide();
            $(`#grant-delete`).closest('.form-check').hide();
            
            // 只显示系统权限
            systemPermissions.forEach(permission => {
                $(`#grant-${permission}`).closest('.form-check').show();
            });
        } else if (grantScopeType === 'TABLE') {
            // 根据表作用范围显示不同的权限类型
            // 先隐藏所有权限
            databasePermissions.forEach(permission => {
                $(`#grant-${permission}`).closest('.form-check').hide();
            });
            $(`#grant-alter-mv`).closest('.form-check').hide();
            $(`#grant-drop-mv`).closest('.form-check').hide();
            $(`#grant-refresh-mv`).closest('.form-check').hide();
            $(`#grant-select-mv`).closest('.form-check').hide();
            // 隐藏系统权限
            systemPermissions.forEach(permission => {
                $(`#grant-${permission}`).closest('.form-check').hide();
            });
            
            if (grantTableScope === 'SINGLE_TABLE') {
                // 单个表显示所有8种权限
                tablePermissions.forEach(permission => {
                    $(`#grant-${permission}`).closest('.form-check').show();
                });
            } else {
                // 多个表只显示数据操作权限（不包含ALTER和DROP）
                tableDataPermissions.forEach(permission => {
                    $(`#grant-${permission}`).closest('.form-check').show();
                });
            }
        }
        
        // 处理撤销权限表单
        if (revokeScopeType === 'DATABASE') {
            // 显示所有数据库权限类型
            databasePermissions.forEach(permission => {
                $(`#revoke-${permission}`).closest('.form-check').show();
            });
            // 隐藏其他类型的权限
            $(`#revoke-alter-mv`).closest('.form-check').hide();
            $(`#revoke-drop-mv`).closest('.form-check').hide();
            $(`#revoke-refresh-mv`).closest('.form-check').hide();
            $(`#revoke-select-mv`).closest('.form-check').hide();
            $(`#revoke-select`).closest('.form-check').hide();
            $(`#revoke-insert`).closest('.form-check').hide();
            $(`#revoke-export`).closest('.form-check').hide();
            $(`#revoke-update`).closest('.form-check').hide();
            $(`#revoke-delete`).closest('.form-check').hide();
            // 隐藏系统权限
            systemPermissions.forEach(permission => {
                $(`#revoke-${permission}`).closest('.form-check').hide();
            });
        } else if (revokeScopeType === 'VIEW') {
            // 只显示视图相关的权限类型
            // 先隐藏所有权限
            databasePermissions.forEach(permission => {
                $(`#revoke-${permission}`).closest('.form-check').hide();
            });
            $(`#revoke-alter-mv`).closest('.form-check').hide();
            $(`#revoke-drop-mv`).closest('.form-check').hide();
            $(`#revoke-refresh-mv`).closest('.form-check').hide();
            $(`#revoke-select-mv`).closest('.form-check').hide();
            $(`#revoke-insert`).closest('.form-check').hide();
            $(`#revoke-export`).closest('.form-check').hide();
            $(`#revoke-update`).closest('.form-check').hide();
            $(`#revoke-delete`).closest('.form-check').hide();
            // 隐藏系统权限
            systemPermissions.forEach(permission => {
                $(`#revoke-${permission}`).closest('.form-check').hide();
            });
            // 显示视图权限
            viewPermissions.forEach(permission => {
                $(`#revoke-${permission}`).closest('.form-check').show();
            });
        } else if (revokeScopeType === 'MATERIALIZED_VIEW') {
            // 只显示物化视图相关的权限类型
            // 先隐藏所有权限
            databasePermissions.forEach(permission => {
                $(`#revoke-${permission}`).closest('.form-check').hide();
            });
            $(`#revoke-insert`).closest('.form-check').hide();
            $(`#revoke-export`).closest('.form-check').hide();
            $(`#revoke-update`).closest('.form-check').hide();
            $(`#revoke-delete`).closest('.form-check').hide();
            // 隐藏系统权限
            systemPermissions.forEach(permission => {
                $(`#revoke-${permission}`).closest('.form-check').hide();
            });
            // 显示物化视图权限
            $(`#revoke-select-mv`).closest('.form-check').show(); // SELECT MATERIALIZED VIEW
            $(`#revoke-alter-mv`).closest('.form-check').show();  // ALTER MATERIALIZED VIEW
            $(`#revoke-refresh-mv`).closest('.form-check').show(); // REFRESH MATERIALIZED VIEW
            $(`#revoke-drop-mv`).closest('.form-check').show();   // DROP MATERIALIZED VIEW
            $(`#revoke-all`).closest('.form-check').show();       // ALL PRIVILEGES
        } else if (revokeScopeType === 'SYSTEM') {
            // 只显示系统相关的权限类型 - 先隐藏所有权限
            
            // 隐藏所有权限选择框 - 使用更简单直接的选择器
            $("input[id^='revoke-'][type='checkbox']").closest('.form-check').hide();
            
            // 显式隐藏所有非系统权限
            databasePermissions.forEach(permission => {
                $(`#revoke-${permission}`).closest('.form-check').hide();
            });
            
            // 隐藏物化视图相关权限
            $(`#revoke-alter-mv`).closest('.form-check').hide();
            $(`#revoke-drop-mv`).closest('.form-check').hide();
            $(`#revoke-refresh-mv`).closest('.form-check').hide();
            $(`#revoke-select-mv`).closest('.form-check').hide();
            
            // 隐藏表相关权限
            $(`#revoke-select`).closest('.form-check').hide();
            $(`#revoke-insert`).closest('.form-check').hide();
            $(`#revoke-export`).closest('.form-check').hide();
            $(`#revoke-update`).closest('.form-check').hide();
            $(`#revoke-delete`).closest('.form-check').hide();
            
            // 只显示系统权限
            systemPermissions.forEach(permission => {
                $(`#revoke-${permission}`).closest('.form-check').show();
            });
        } else if (revokeScopeType === 'TABLE') {
            // 根据表作用范围显示不同的权限类型
            // 先隐藏所有权限
            databasePermissions.forEach(permission => {
                $(`#revoke-${permission}`).closest('.form-check').hide();
            });
            $(`#revoke-alter-mv`).closest('.form-check').hide();
            $(`#revoke-drop-mv`).closest('.form-check').hide();
            $(`#revoke-refresh-mv`).closest('.form-check').hide();
            $(`#revoke-select-mv`).closest('.form-check').hide();
            // 隐藏系统权限
            systemPermissions.forEach(permission => {
                $(`#revoke-${permission}`).closest('.form-check').hide();
            });
            
            if (revokeTableScope === 'SINGLE_TABLE') {
                // 单个表显示所有8种权限
                tablePermissions.forEach(permission => {
                    $(`#revoke-${permission}`).closest('.form-check').show();
                });
            } else {
                // 多个表只显示数据操作权限（不包含ALTER和DROP）
                tableDataPermissions.forEach(permission => {
                    $(`#revoke-${permission}`).closest('.form-check').show();
                });
            }
        }
    }

    // 处理撤销权限
    function handleRevokePermission() {
        const formData = PermissionManager.getRevokeFormData();
        
        // 验证权限类型是否选择
        if (!PermissionManager.validatePermissionTypes(formData.permissionTypes)) {
            UIUtils.showMessage('错误', '请至少选择一种权限类型', 'danger');
            return;
        }

        const submitBtn = $('#revoke-submit-btn');
        UIUtils.setButtonLoading(submitBtn, true, '处理中...', '<i class="fas fa-times mr-2"></i>撤销权限');

        PermissionManager.revokePermission(formData)
            .done(function(response) {
                if (response.code === 200) {
                    UIUtils.showMessage('成功', '权限撤销成功', 'success');
                    PermissionManager.resetRevokeForm();
                    loadUserList();
                } else {
                    UIUtils.showMessage('错误', response.message, 'danger');
                }
            })
            .fail(function(xhr, status, error) {
                console.error('撤销权限失败:', error);
                UIUtils.showMessage('错误', '撤销权限失败: ' + xhr.responseText, 'danger');
            })
            .always(function() {
                UIUtils.setButtonLoading(submitBtn, false, '', '<i class="fas fa-times mr-2"></i>撤销权限');
            });
    }

    // 显示用户权限详情（全局函数，供HTML中的onclick调用）
    window.showUserPermissions = function(username, host) {
        UIUtils.showLoading('user-permissions-content', '加载权限详情...');
        
        UserManager.showUserPermissions(username, host)
            .done(function(response) {
                if (response.code === 200) {
                    UserManager.renderUserPermissions(response.data);
                    UIUtils.showModal('user-permissions-modal');
                } else {
                    UIUtils.showMessage('错误', response.message, 'danger');
                }
            })
            .fail(function(xhr, status, error) {
                console.error('加载用户权限详情失败:', error);
                UIUtils.showMessage('错误', '加载用户权限详情失败: ' + xhr.responseText, 'danger');
            });
    };

    return {
        init
    };
})();

// 页面加载完成后初始化应用
    $(document).ready(function() {
        App.init();
        
        // 初始化函数 - 确保所有输入框的required属性正确设置
        function initRequiredAttributes() {
            // 先移除所有输入框的required属性
            $('#grant-database, #grant-table-name, #grant-table-database, #grant-view-name, #grant-view-database, #revoke-database, #revoke-table-name, #revoke-table-database, #revoke-view-name, #revoke-view-database').removeAttr('required');
            
            // 为当前选中的作用范围添加相应的required属性
            const grantScopeType = $('#grant-scope-type').val();
            if (grantScopeType === 'DATABASE') {
                $('#grant-database').attr('required', 'required');
            }
            
            const revokeScopeType = $('#revoke-scope-type').val();
            if (revokeScopeType === 'DATABASE') {
                $('#revoke-database').attr('required', 'required');
            }
        }
        
        // 初始调用
        initRequiredAttributes();
        
        // 为两个表单添加submit事件监听，在提交前重新检查required属性
        $('#grant-permission-form, #revoke-permission-form').on('submit', function(e) {
            // 移除所有输入框的required属性
            $('#grant-database, #grant-table-name, #grant-table-database, #grant-view-name, #grant-view-database, #revoke-database, #revoke-table-name, #revoke-table-database, #revoke-view-name, #revoke-view-database').removeAttr('required');
            
            // 根据当前表单和作用范围添加必要的required属性
            const formId = this.id.includes('grant') ? 'grant' : 'revoke';
            const scopeType = $(`#${formId}-scope-type`).val();
            
            if (scopeType === 'DATABASE') {
                $(`#${formId}-database`).attr('required', 'required');
            } else if (scopeType === 'TABLE') {
                const tableScope = $(`#${formId}-table-scope`).val();
                if (tableScope === 'SINGLE_TABLE') {
                    $(`#${formId}-table-name`).attr('required', 'required');
                    $(`#${formId}-table-database`).attr('required', 'required');
                } else if (tableScope === 'ALL_TABLES_IN_DATABASE') {
                    $(`#${formId}-table-database`).attr('required', 'required');
                }
            } else if (scopeType === 'VIEW' || scopeType === 'MATERIALIZED_VIEW') {
                const viewScope = $(`#${formId}-view-scope`).val();
                if (viewScope === 'SINGLE_VIEW') {
                    $(`#${formId}-view-name`).attr('required', 'required');
                    $(`#${formId}-view-database`).attr('required', 'required');
                } else if (viewScope === 'ALL_VIEWS_IN_DATABASE') {
                    $(`#${formId}-view-database`).attr('required', 'required');
                }
            }
            
            // 不阻止表单提交，让浏览器继续验证有required属性的输入框
        });
    });