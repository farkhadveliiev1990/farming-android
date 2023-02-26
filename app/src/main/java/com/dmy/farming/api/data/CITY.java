package com.dmy.farming.api.data;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;


@Table(name = "CITY")
public class CITY extends Model {

	@Column(name = "name")
	public String name;

	@Column(name = "pinyin")
	public String pinyin;

	public CITY init(String name, String pinyin)
	{
		this.name = name;
		this.pinyin = pinyin;
		return this;
	}
}
