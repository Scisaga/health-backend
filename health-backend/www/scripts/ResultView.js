var ResultView = Backbone.View.extend({

	tagName: 'div',
	className: 'row',
	events: {
		
	},

	initialize: function() {

		var that = this;

		$.ajax({
			url: '/templates/ResultView.html', 
			async: false,
			success: function(data) {

				that.$el.html(data);
			}
		});

		this.limit = 10;
		this.offset = 0;

	},

	render: function(section_id, service, preference, log, lat) {

		var that = this;

		this.section_id = parseInt(section_id);
		this.service = service;
		this.preference = parseInt(preference);
		this.log = parseFloat(log);
		this.lat = parseFloat(lat);

		this.query();
		
		$(document).unbind('scroll');
        $(document).on('scroll', function() {
            
            if($(this).scrollTop() + $(window).innerHeight() >= $(this).innerHeight() )  {
                that.offset += that.limit;
                that.query();
            }
        });
	},

	query: function() {
		
		var that = this;

		$.ajax({
			url: '/query',
			type: 'POST',
			dataType: "json",
			async: false,
			data: {
				_q: JSON.stringify({
					section_id: this.section_id,
					service: this.service,
					preference: this.preference,
					log: this.log,
					lat: this.lat,
					offset: this.offset,
					limit: this.limit
				})
			},
			crossDomain: true,
			success: function (result) {

				if(result.code == 1){
					
					var top = $(document).innerHeight();

					_.each(result.data, function(item){

						//console.log(item);
						var itemView = new ResultItemView();
						itemView.render(item);
						that.$el.append(itemView.$el);
					});

					if(result.data.length > 0) {

						var n = noty({
							text: '更新结果：' + result.data.length + '条',
							type: 'success'
						});

						//$(document).scrollTop(top - 100);
					}
				}
				else {
					var n = noty({
						text: '查询失败',
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

var ResultItemView = Backbone.View.extend({

	tagName: 'div',
	className: 'col-xs-12 col-md-6 col-md-offset-3',
	events: {
		
	},

	initialize: function() {

		var that = this;

		$.ajax({
			url: '/templates/ResultItemView.html', 
			async: false,
			success: function(data) {

				that.template = _.template(data);
			}
		});
	},

	render: function(item) {
		this.$el.html(this.template(item));
		this.$('.result-item').animate({
			backgroundColor: "#f7f7f7"
		}, 1000);
	},

	query: function() {

	}
});