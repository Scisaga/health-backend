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

		var comment_create_view = new CommentCreateView();

		comment_create_view.render();

		console.log(comment_create_view.$el);
		this.$el.append(comment_create_view.$el);

	},
});

var CommentView = Backbone.View.extend({

	tagName: 'div',
	className: 'comment col-xs-12 col-md-6 col-md-offset-3',
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

var CommentCreateView = Backbone.View.extend({

	tagName: 'div',
	className: 'comment col-xs-12 col-md-6 col-md-offset-3',
	events: {
		'click .save' : 'save'
	},

	initialize: function() {

		var that = this;

		$.ajax({
			url: '/templates/CommentCreateView.html', 
			async: false,
			success: function(data) {
				that.template = _.template(data);
			}
		});

	},

	render: function() {

		var that = this;
		this.$el.html(this.template());
		this.$('input.rating').rating();
	},

	
	save: function(){


	}
});