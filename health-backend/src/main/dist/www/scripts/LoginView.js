var LoginView = Backbone.View.extend({

	tagName: 'div',
	className: 'row',

	initialize: function() {
		var that = this;
		
		$.ajax({
			url: '/templates/LoginView.html', 
			async: false,
			success: function(data) {
				that.$el.append(data);
				that.$("input[name='remember-me']").bootstrapSwitch({
					size: 'small',
				});
			}
		});
	},

	render: function() {

		var that = this;

		this.$('#inputPassword').bind('keypress', function (event) {
			if(event.keyCode == '13'){
				that.$('button').trigger('click');
			}
		});

		$(this.$el).bootstrapValidator({
			fields: {
				username: {
					validators: {
						notEmpty: {
							message: '用户名不能为空'
						},
						regexp: {
	                        regexp: /^[a-zA-Z0-9]+$/i,
	                        message: '用户名只能由英文和数字构成'
	                    },
	                    stringLength: {
	                    	min: 6,
	                        max: 32,
	                        message: '用户名长度需要大于6个字符，小于32个字符'
	                    }
					}
				},
				passwd: {
					validators: {
						notEmpty: {
							message: '密码不能为空'
						},
						stringLength: {
	                    	min: 8,
	                        max: 32,
	                        message: '密码长度需要大于8个字符，小于32个字符'
	                    }
					}
				}
			}
		});

		this.validator = $(this.$el).data('bootstrapValidator');

		this.$('button.signup').click(function(){

    		that.validator.validate();

    		if(that.validator.isValid()) {


				var username = that.$('#inputUsername').val();
				var passwd = that.$('#inputPassword').val();

    			$.ajax({
					url: '/users',
					type: 'POST',
					dataType: "json",
					data: {
						_q: JSON.stringify({
							name: username,
							password: passwd
						})
					},
					crossDomain: true,
					success: function (result) {

						if(result.code == 111){
							var n = noty({
								text: '注册成功',
								type: 'success'
							});

							Cookies.set('uid', result.data);

							if(that.$('input:checkbox')[0].checked){
								Cookies.set('username', email);
							} else {
								Cookies.remove('username');
							}

							window.app.navigate("user", {trigger: true});
						}
						else {
							var n = noty({
								text: '注册失败',
								type: 'error'
							});
						}
						
					},
					error: function (xhr, ajaxOptions, thrownError) {
						var n = noty({
							text: '后台错误',
							type: 'error'
						});
					}
				});
    		}

		});

		this.$('button.login').click(function(){

    		that.validator.validate();

    		if(that.validator.isValid()) {

				var username = that.$('#inputUsername').val();
				var passwd = that.$('#inputPassword').val();

    			$.ajax({
					url: '/login',
					type: 'POST',
					dataType: "json",
					data: {
						_q: JSON.stringify({
							name: username,
							password: passwd
						})
					},
					crossDomain: true,
					success: function (result) {

						if(result.code == 1){
							var n = noty({
								text: '登陆成功',
								type: 'success'
							});

							Cookies.set('uid', result.data);

							if(that.$('input:checkbox')[0].checked){
								Cookies.set('username', email);
							} else {
								Cookies.remove('username');
							}

							window.app.navigate("query", {trigger: true});
						}
						else {
							var n = noty({
								text: '登陆失败',
								type: 'error'
							});
						}
						
					},
					error: function (xhr, ajaxOptions, thrownError) {
						var n = noty({
							text: '后台错误',
							type: 'error'
						});
					}
				});
    		}
			
		});

		if(Cookies.get('username') != null) {
			that.$('#inputUsername').val(Cookies.get('username'));
			$(that.$('input:checkbox')[0]).attr({
				checked: true
			});
		}
	},

	showLoginForm: function(){
		this.$('#intro').hide();
		this.$('.slide').hide();
		this.$('#login-form').show();
	}
});