var Doctor = Backbone.Model.extend({
	urlRoot : '/doctors',
	parse : function (resp, options) {
	 	return resp.data;
	}
});

var DoctorView = Backbone.View.extend({

	tagName: 'div',
	className: 'row doctor',
	events: {
		'click .save' : 'save'
	},

	initialize: function() {

		var that = this;

		$.ajax({
			url: '/templates/DoctorView.html', 
			async: false,
			success: function(data) {
				that.template = _.template(data);
			}
		});

	},

	render: function() {

		var that = this;

		this.$el.html(this.template(this.model.attributes));
	},
});