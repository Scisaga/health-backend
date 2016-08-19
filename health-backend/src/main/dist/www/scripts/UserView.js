var User = Backbone.Model.extend({
	urlRoot : '/users',
	parse : function (resp, options) {
	 	return resp.data;
	}
});

var UserView = Backbone.View.extend({

	tagName: 'div',
	className: 'row',
	events: {
		'click .save' : 'save'
	},

	initialize: function() {

		var that = this;

		$.ajax({
			url: '/templates/UserView.html', 
			async: false,
			success: function(data) {
				that.template = _.template(data);
			}
		});

	},

	render: function() {

		var that = this;

		this.$el.html(this.template(this.model.attributes));

		_.each(this.$(':input'), function(input){

            $(input).on('change', function (e) {

            	that.model.set(input.name, $(input).val());
            });
        });
	},

	save: function() {

		this.model.save(this.model.attributes, {

			success: function (model, res) {
				if(res.code == 112){
					var n = noty({
						text: '保存成功',
						type: 'success'
					});
				}
				else {
					var n = noty({
						text: '保存失败',
						type: 'error'
					});
				}
				
			}
		});
		
		this.render();
	}
});