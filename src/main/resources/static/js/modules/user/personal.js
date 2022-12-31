var app =  new Vue({
    el: '#app',
    data:{
        ruleForm: {
            id:'',
            name: '',
            nickName: '',
            sex: '',
            userRole: '',
            mobile: '',
            email: '',
            birthday: '',
            hobby: '',
            liveAddress: ''
        },
        rules: {
            name: [
                { required: true, message: '请输入用户名', trigger: 'blur' }
            ]
        }
    },
    methods: {
        submitForm:function(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    if (isMobileEmail(app.ruleForm.mobile,app.ruleForm.email)){
                        $.ajax({
                            url: context + '/user/editUser',
                            type: 'POST',
                            data: JSON.stringify(app.ruleForm),
                            dataType : 'json',
                            contentType:'application/json',
                            success: function (res) {
                                if (res.code === 200) {
                                    app.$message.success(res.message);
                                } else {
                                    app.$message.error(res.message);
                                }
                            }
                        })
                    }
                } else {
                    console.log('error submit!!');
                    return false;
                }
             });
        }
    },
    mounted:function() {
        var sysUser = JSON.parse($("#sysUser").val());
        this.ruleForm.id = sysUser.id;
        this.ruleForm.name = sysUser.name;
        this.ruleForm.nickName = sysUser.nickName;
        this.ruleForm.sex = sysUser.sex;
        this.ruleForm.userRole = $("#roleName").val();
        this.ruleForm.mobile = sysUser.mobile;
        this.ruleForm.email = sysUser.email;
        this.ruleForm.birthday = sysUser.birthday;
        this.ruleForm.hobby = sysUser.hobby;
        this.ruleForm.liveAddress = sysUser.liveAddress;
    }
});

function isMobileEmail(mobile,email) {
    var flag = true;
    if (mobile !== ""){
        var phoneReg = /^[1][3,4,5,7,8][0-9]{9}$/;
        if (!phoneReg.test(mobile)) {
            app.$message.error("手机号格式不正确，请重新输入！");
            app.ruleForm.mobile = "";
            flag =  false;
        }
    }
    if(email !== "") {
        var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
        //调用正则验证test()函数
        isok= reg.test(email);
        if(!isok) {
            app.$message.error("邮箱格式不正确，请重新输入！");
            app.ruleForm.email = "";
            flag = false;
        }
    }
    return flag;
}
