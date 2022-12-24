var vm = new Vue({
    el: '#LAY_app',
    data: {
        loginName: '',
        menuList: ''
    },
    methods: {
        logout: function () {
            var localObj = window.location;
            location.href = 'http://' + location.host + "/" + _ctx + "/logout";
        }
    },
    mounted: function () {
        $.ajax({
            url: _ctx + '/menu/getMenulist',
            type: 'GET',
            success: function (res) {
                vm.loginName = res.data.name;
                vm.menuList = res.data.menuList;
            }
        })
    }

});