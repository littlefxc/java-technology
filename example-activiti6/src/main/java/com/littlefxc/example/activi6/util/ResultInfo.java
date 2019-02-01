package com.littlefxc.example.activi6.util;

import java.io.Serializable;

/**
 * 返回结果信息类
 */
public class ResultInfo<T> {

	// 返回状态
	private Integer code;
	//状态描述信息
	private String msg;
	//Info查询总数据量
	private long count;
	//返回数据列表
	private T info;
	
	public Integer getCode() {
	    return code;
	}
	public void setCode(Integer code) {
	    this.code = code;
	}
	
	public String getMsg() {
	    return msg;
	}

	public void setMsg(String msg) {
	    this.msg = msg;
	}

	
	public long getCount() {
	    return count;
	}

	public void setCount(long count) {
	    this.count = count;
	}

	public Object getInfo() {
	    return info;
	}

	public void setInfo(T info) {
	    this.info = info;
	}

	public ResultInfo() {
		super();
	}

	@Override
	public String toString() {
		return "ResultInfo{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", count=" + count +
				", info=" + info +
				'}';
	}
}
