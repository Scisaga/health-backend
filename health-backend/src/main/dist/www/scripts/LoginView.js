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
			}
		});
	},

	render: function() {

		this.$('#inputPassword').bind('keypress', function (event) {
			if(event.keyCode == '13'){
				that.$('button').trigger('click');
			}
		});

		this.$('button.signup').click(function(){

			var username = that.$('#inputUsername').val();
			var passwd = that.$('#inputPassword').val();
			

			// 获取token
			$.ajax({
				url: '/user',
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

			if(that.$('input:checkbox')[0].checked){
				Cookies.set('username', email);
			} else {
				Cookies.remove('username');
			}

		});

		this.$('button.login').click(function(){

			var $button = $(this);

			$(this).isLoading({
				text: "登陆中",
				position: "inside",
				disableOthers: [
					that.$('#inputUsername'),
					that.$('#inputPassword'),
					that.$('button.signup')
				]
			});

			var username = that.$('#inputUsername').val();
			var passwd = that.$('#inputPassword').val();
			

			// 获取token
			$.ajax({
				url: '/wiki/api.php',
				type: 'POST',
				dataType: "json",
				data: {
					action: 'login',
					lgname: username,
					format: 'json'
				},
				crossDomain: true,
				success: function (result) {

					var token = result.login.token;

					// 登陆
					$.ajax({
						url: '/wiki/api.php',
						type: 'POST',
						dataType: "json",
						data: {
							action: 'login',
							lgname: username,
							lgpassword: passwd,
							lgtoken: token,
							format: 'json'
						},
						crossDomain: true,
						success: function (result) {

							if(result.login.result == 'Success'){

								Cookies.set('uid', result.login.lguserid, { expires: 7 });
								
								app.navigate("nodes", {trigger: true});
								
								//location.reload();

							} else {
								var n = noty({
									text: result.login.result,
									type: 'error'
								});

								$button.isLoading("hide").text('登陆');
							}

						},
						error: function (xhr, ajaxOptions, thrownError) {
							var n = noty({
								text: '登陆失败',
								type: 'error'
							});
							$button.isLoading("hide").text('登陆');
						}
					});
				},
				error: function (xhr, ajaxOptions, thrownError) {
					var n = noty({
						text: 'Wiki后台没有反应',
						type: 'error'
					});
					$button.isLoading("hide").text('登陆');
				}
			});

			if(that.$('input:checkbox')[0].checked){
				Cookies.set('username', email);
			} else {
				Cookies.remove('username');
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