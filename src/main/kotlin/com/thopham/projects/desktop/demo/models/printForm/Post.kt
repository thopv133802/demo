/* 
Copyright (c) 2019 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */
package com.thopham.projects.desktop.demo.models.printForm

data class Post (

		val res_id : Int,
		val id_promotion : String?,
		val cus_name : String,
		val telephone : String,
		val method : String,
		val typeorder : Int,
		val order_type : String,
		val delivery_type : String,
		val detail : List<Detail>,
		val cus_id : Int,
		val page_id : Int,
		val cus_mail : String,
		val total_price_pay : Int,
		val discount : String,
		val supplementary : String,
		val vat : String,
		val total_price_end : Int,
		val input_money : Int,
		val money_change : String,
		val note : String,
		val deliver : Int,
		val position : String,
		val id_bill : Int,
		val table_floor : String
){
	constructor(): this(0, null, "", "", "", 0,
			"", "", listOf<Detail>(), 0, 0, "", 0, "", "", "", 0, 0, "", ""
			, 0, "", 0, ""
	)
}