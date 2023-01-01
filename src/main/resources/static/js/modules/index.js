var vm = new Vue({
    el: '#LAY_app',
    data: {
        loginName: '',
        menuList: ''
    },
    methods: {
        logout: function () {
            location.href = 'http://' + location.host + "/" + context + "/logout";
        }
    },
    mounted: function () {
        $.ajax({
            url: context + '/menu/getMenulist',
            type: 'GET',
            success: function (res) {
                vm.loginName = res.data.name;
                vm.menuList = res.data.menuList;
            }
        })
    }

});