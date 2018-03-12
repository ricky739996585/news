function pageTools(curtpage, tpage) {
	var options = {
		bootstrapMajorVersion : 3, //版本  
		currentPage : curtpage, //当前页数  
		numberOfPages : 20, //设置显示的页码数  
		totalPages : tpage, //总页数  
		alignment : "center", // 居中显示
		itemTexts : function(type, page, current) {
			switch (type) {
			case "first":
				return "首页";
			case "prev":
				return "上一页";
			case "next":
				return "下一页";
			case "last":
				return "末页";
			case "page":
				return page;
			}
		},
		onPageClicked : function(event, originalEvent, type, page) {
			getList(page); //  在页面中的方法名称。
		}
	}
	$("#pagintor").bootstrapPaginator(options); // $("#pagintor") Bootstrap 是2.X 使用div元素，3.X使用ul元素
}

function addToDB(url, data) {
	$.ajax({
		url : url,
		type : "post",
		data : data,
		success : function(data) {
			if (data.statusCode == "200") {

			} else {
				alert("add error!" + data.statusMsg);
			}
		},
		error : function(data) {
			alert(JSON.stringify(data));
		}
	})
}

//获取url中的参数
function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg); //匹配目标参数
	if (r != null) return unescape(r[2]);
	return null; //返回参数值
}