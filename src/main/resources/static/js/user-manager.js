// 用户管理模块

const UserManager = (function() {
    const API_BASE_URL = '/star-guard/api/permission';

    // 加载用户列表
    function loadUserList() {
        return $.ajax({
            url: `${API_BASE_URL}/users`,
            type: 'GET',
            dataType: 'json'
        });
    }

    // 渲染用户列表
    function renderUserList(users) {
        const tbody = $('#user-list-body');
        tbody.empty();

        if (users && users.length > 0) {
            users.forEach(function(user) {
                const hasGrantOption = user.hasGrantOption ?
                    '<span class="badge badge-success">是</span>' :
                    '<span class="badge badge-secondary">否</span>';
                const permissionCount = user.permissions ? user.permissions.length : 0;
                const permissionCountBadge = permissionCount > 0 ?
                    `<span class="badge badge-info">${permissionCount}</span>` :
                    '<span class="badge badge-warning">0</span>';

                const row = `<tr>
                    <td>${user.username}</td>
                    <td>${user.host}</td>
                    <td>${permissionCountBadge}</td>
                    <td>${hasGrantOption}</td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="showUserPermissions('${encodeURIComponent(user.username)}', '${encodeURIComponent(user.host)}')">
                            <i class="fas fa-eye mr-1"></i>查看权限
                        </button>
                    </td>
                </tr>`;
                tbody.append(row);
            });
        } else {
            tbody.append('<tr><td colspan="5" class="text-center">暂无用户数据</td></tr>');
        }
    }

    // 显示用户权限详情
    function showUserPermissions(username, host) {
        // 注意：需要对接收到的编码参数进行解码，以避免浏览器在URL中自动再次编码
        const decodedUsername = decodeURIComponent(username);
        const decodedHost = decodeURIComponent(host);
        return $.ajax({
            url: `${API_BASE_URL}/users/${encodeURIComponent(decodedUsername)}/${encodeURIComponent(decodedHost)}`,
            type: 'GET',
            dataType: 'json'
        });
    }

    // 渲染用户权限详情
    function renderUserPermissions(user) {
        const content = $('#user-permissions-content');
        content.empty();

        const header = `<div class="mb-4">
            <h5><strong>用户名:</strong> ${user.username}</h5>
            <h5><strong>主机地址:</strong> ${user.host}</h5>
            <h5><strong>是否有Grant Option:</strong> ${user.hasGrantOption ? '<span class="text-success">是</span>' : '<span class="text-secondary">否</span>'}</h5>
        </div>`;
        content.append(header);

        if (user.permissions && user.permissions.length > 0) {
            const permissionsTable = `<div class="table-responsive">
                <table class="table table-striped table-sm">
                    <thead class="thead-light">
                        <tr>
                            <th>权限类型</th>
                            <th>数据库</th>
                            <th>所有数据库</th>
                            <th>With Grant Option</th>
                        </tr>
                    </thead>
                    <tbody>`;
            content.append(permissionsTable);

            user.permissions.forEach(function(permission) {
                const row = `<tr>
                    <td><span class="badge badge-primary">${permission.permissionType}</span></td>
                    <td>${permission.databaseName}</td>
                    <td><span class="badge ${permission.allDatabases ? 'badge-success' : 'badge-secondary'} p-1">${permission.allDatabases ? '是' : '否'}</span></td>
                    <td><span class="badge ${permission.withGrantOption ? 'badge-warning' : 'badge-secondary'} p-1">${permission.withGrantOption ? '是' : '否'}</span></td>
                </tr>`;
                content.append(row);
            });

            content.append(`</tbody>
                </table>
            </div>`);
        } else {
            content.append('<div class="alert alert-info">该用户暂无权限</div>');
        }
    }

    return {
        loadUserList,
        renderUserList,
        showUserPermissions,
        renderUserPermissions
    };
})();