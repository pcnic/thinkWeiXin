package com.linjj.thinkweixin.vo;

public class ZSequence {
//	开始行数
    private Integer start;
    //结束行数
    private Integer limit;
	public Integer getLimit() {return limit;}
	public void setLimit(Integer limit) {this.limit = limit;}
	public Integer getStart() {return start;}
	public void setStart(Integer start) {this.start = start;}
    //TNAME z_sequence.TNAME
    private String TNAME;
    public String getTNAME() {return TNAME;}
    public void setTNAME(String TNAME) {this.TNAME = TNAME;}
  
    //SEQVALUE z_sequence.SEQVALUE
    private String SEQVALUE;
    public String getSEQVALUE() {return SEQVALUE;}
    public void setSEQVALUE(String SEQVALUE) {this.SEQVALUE = SEQVALUE;}
  
    private boolean ISDEL;//判断记录的删除
	private boolean ISUPDATE;//判断记录的修改
	public boolean isISDEL() {
		return ISDEL;
	}
	public void setISDEL(boolean isdel) {
		ISDEL = isdel;
	}
	public boolean isISUPDATE() {
		return ISUPDATE;
	}
	public void setISUPDATE(boolean isupdate) {
		ISUPDATE = isupdate;
	} 

/*BEGIN*/
//注意请勿修改BEGIN和END两行!!!!!!
/*请在此处进行所需要的修改,出了BEGIN END注释块进行的修改将在下一次生成时被覆盖*/
/*END*/

}

