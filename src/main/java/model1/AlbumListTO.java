package model1;

import java.util.ArrayList;

public class AlbumListTO {
	private int cpage;
	private int totalRecord;
	private int totalPages;
	private int recordPerPage;
	private int startBlock;
	private int endBlock;
	private int blockPerPage;
	private ArrayList<AlbumTO> boardLists;
	private String nickname;
	private String type;
	private String catSeq;
	private Boolean isPrivate;
	
	//생성자로 초기화
	public AlbumListTO() {
		// TODO Auto-generated constructor stub
		
		this.cpage = 1;
		this.recordPerPage = 6;
		this.blockPerPage = 5;
		this.totalPages = 1;
		this.totalRecord = 0;
	
	}
	
	
	public int getCpage() {
		return cpage;
	}
	public void setCpage(int cpage) {
		this.cpage = cpage;
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getRecordPerPage() {
		return recordPerPage;
	}
	public void setRecordPerPage(int recordPerPage) {
		this.recordPerPage = recordPerPage;
	}
	public int getStartBlock() {
		return startBlock;
	}
	public void setStartBlock(int startBlock) {
		this.startBlock = startBlock;
	}
	public int getEndBlock() {
		return endBlock;
	}
	public void setEndBlock(int endBlock) {
		this.endBlock = endBlock;
	}
	public int getBlockPerPage() {
		return blockPerPage;
	}
	public void setBlockPerPage(int blockPerPage) {
		this.blockPerPage = blockPerPage;
	}
	public ArrayList<AlbumTO> getBoardLists() {
		return boardLists;
	}
	public void setBoardLists(ArrayList<AlbumTO> boardLists) {
		this.boardLists = boardLists;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCatSeq() {
		return catSeq;
	}
	public void setCatSeq(String catSeq) {
		this.catSeq = catSeq;
	}
	public Boolean getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	
	
}
