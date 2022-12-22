var vm = new Vue({
    el: '#app',
    data: {
        tableData: [],
        total: 50,
        page_size: 5,
        current_page: 1
    },
    methods: {
        handleSizeChange: function (val) {
            vm.page_size = val;
            this.geSysLoglist();
        },
        handleCurrentChange: function (val) {
            vm.current_page = val;
            this.geSysLoglist();
        },
        reload: function(){
            this.geSysLoglist();
        },
        geSysLoglist: function () {
            $.ajax({
                url: context + 'sysLog/geSysLoglist?page=' + this.current_page + '&page_size=' + this.page_size,
                type: 'GET',
                success: function (res) {
                    console.log(res);
                    vm.tableData = res.data.sysLogList;
                    vm.total = res.data.total;
                    vm.page_size = res.data.page_size;
                    vm.current_page = res.data.page;
                }
            });
        }

    },
    mounted: function () {
        this.geSysLoglist();
    }
});
