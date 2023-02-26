package com.dmy.farming.api.data;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;


@Table(name = "RECENTCITY")
public class RECENTCITY extends Model {

	@Column(name = "name", unique = true)
	public String name;

	@Column
	public long date;

	public RECENTCITY init(String name, long date)
	{
		this.name = name;
		this.date = date;
		return this;
	}
}
