// UI工具模块
const UIUtils = (function() {
    // 显示操作结果消息
    function showMessage(title, content, type = 'success') {
        const alert = $('#message-alert');
        $('#message-title').text(title);
        $('#message-content').text(content);

        // 设置消息类型样式
        alert.removeClass('alert-success alert-danger alert-warning alert-info');
        alert.addClass(`alert-${type}`);

        // 显示消息
        alert.fadeIn();

        // 3秒后自动隐藏
        setTimeout(function() {
            alert.fadeOut();
        }, 3000);
    }

    // 隐藏消息
    function hideMessage() {
        $('#message-alert').fadeOut();
    }

    // 设置按钮加载状态
    function setButtonLoading(button, isLoading, loadingText = '处理中...', originalText = '') {
        if (isLoading) {
            button.prop('disabled', true);
            button.html(`<i class="fas fa-spinner fa-spin mr-2"></i>${loadingText}`);
        } else {
            button.prop('disabled', false);
            button.html(originalText);
        }
    }

    // 显示模态框
    function showModal(modalId) {
        $(`#${modalId}`).modal('show');
    }

    // 隐藏模态框
    function hideModal(modalId) {
        $(`#${modalId}`).modal('hide');
    }

    // 清空模态框内容
    function clearModalContent(modalContentId) {
        $(`#${modalContentId}`).empty();
    }

    // 显示加载状态
    function showLoading(containerId, message = '加载中...') {
        $(`#${containerId}`).html(`
            <div class="text-center">
                <div class="spinner-border text-primary" role="status">
                    <span class="sr-only">${message}</span>
                </div>
                <p class="mt-2">${message}</p>
            </div>
        `);
    }

    // 显示错误状态
    function showError(containerId, message = '加载失败') {
        $(`#${containerId}`).html(`
            <div class="text-center text-danger">
                <i class="fas fa-exclamation-triangle fa-2x mb-2"></i>
                <p>${message}</p>
            </div>
        `);
    }

    return {
        showMessage,
        hideMessage,
        setButtonLoading,
        showModal,
        hideModal,
        clearModalContent,
        showLoading,
        showError
    };
})();