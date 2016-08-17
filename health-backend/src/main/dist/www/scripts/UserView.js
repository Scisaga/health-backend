var UserView = Backbone.View.extend({

	tagName: 'div',
	className: 'row',

	initialize: function() {

		$.ajax({
			url: '/templates/UserView.html', 
			async: false,
			success: function(data) {
				that.$el.append(data);
			}
		});

	},

	render: function() {

		var that = this;
		
		$.ajax({
			url: '/templates/UserView.html', 
			async: false,
			success: function(data) {
				that.$el.append(data);
			}
		});
		
		this.$('button.save').click(function(){

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


		if(Cookies.get('username') != null) {
			that.$('#inputUsername').val(Cookies.get('username'));
			$(that.$('input:checkbox')[0]).attr({
				checked: true
			});
		}
	}
});