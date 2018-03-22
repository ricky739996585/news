var listURL = "/advertisement/getAdminAdvertisementList";
var editURL = "/advertisement/editAdvertisement";
$('#tableList').bootstrapTable({
	url : listURL, // 请求后台的URL（*）
	method : 'POST', // 请求方式（*）
	totalField : 'total',
	dataField : 'list',
	// toolbar : '#toolbar', //工具按钮用哪个容器
	striped : true, // 是否显示行间隔色
	cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	pagination : true, // 是否显示分页（*）
	// sortable : false, //是否启用排序
	// sortOrder : "asc", //排序方式
	contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
	dataType : 'json',
	queryParams : function(params) {
		var pageNo = params.offset / params.limit + 1;
		var keyword = $("#keyword").val();
		var order = $("#order").val();
		var desc = $("input[name='desc']:checked").val();
		var tbstatus = $("#tbstatus").val();
		var temp = {
			pageSize : params.limit, // 页面大小
			pageNo : pageNo, // 页码
			keyword : keyword,
			order : order,
			desc : desc,
			tbstatus : tbstatus
		};
		return temp;
	},// 传递参数（*）
	sidePagination : "server", // 分页方式：client客户端分页，server服务端分页（*）
	pageNumber : 1, // 初始化加载第一页，默认第一页
	pageSize : 10, // 每页的记录行数（*）
	pageList : [ 10, 15, 30, 50 ], // 可供选择的每页的行数（*）
	search : false, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
	strictSearch : false,
	showColumns : true, // 是否显示所有的列
	showRefresh : true, // 是否显示刷新按钮
	minimumCountColumns : 2, // 最少允许的列数
	clickToSelect : true, // 是否启用点击选中行
	// height : 550, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
	uniqueId : "advertisementId", // 每一行的唯一标识，一般为主键列
	showToggle : true, // 是否显示详细视图和列表视图的切换按钮
	cardView : false, // 是否显示详细视图
	detailView : false, // 是否显示父子表
	columns : [ 
		{field : "advertisementId", title : "主键id"},
		{field : "title", title : "轮播图内容主题"
			//,editable : { type : "text",title : "轮播图内容主题",validate : function(v) {if (!v) {return "轮播图内容主题不能为空";}}}
		},
		{field : "pic", title : "图片url"
			//,editable : { type : "text",title : "图片url",validate : function(v) {if (!v) {return "图片url不能为空";}}}
		},
		{field : "targetUrl", title : "图片跳转的地址"
			//,editable : { type : "text",title : "图片跳转的地址",validate : function(v) {if (!v) {return "图片跳转的地址不能为空";}}}
		},
		{field : "orderNum", title : "顺序"
			//,editable : { type : "text",title : "顺序",validate : function(v) {if (!v) {return "顺序不能为空";}}}
		},
		{field : "advertisementType", title : "广告类型"
			//,editable : { type : "text",title : "广告类型",validate : function(v) {if (!v) {return "广告类型不能为空";}}}
		},
		{field : "createTime", title : "创建时间"},
		{field : "modifyTime", title : "修改时间"},
		{field : "tbstatus", title : "状态"
			,editable : { type : "select",title : "状态",source : [ {value : "正常",text : "正常"},{value : "删除",text : "删除"}]}
		}
		,{field : "operate", title : "操作",align: "center",
			formatter:function(value,row,index){
				var e = '<a href="/admin/advertisement/addOrEditAdvertisement.html?advertisementId=' + row.advertisementId + '" target="_blank" >编辑</a> ';
				return e;
			}
		}
	],
	onEditableSave : function(field, row, oldValue, $el) {
		$.ajax({
			type : "post",
			url : editURL,
			data : row,
			dataType : 'JSON',
			success : function(data, tbstatus) {
				if (tbstatus == "success") {
					alert('提交数据成功');
				}
			},
			error : function() {
				alert('编辑失败');
			},
			complete : function() {

			}
		});
	}
});

