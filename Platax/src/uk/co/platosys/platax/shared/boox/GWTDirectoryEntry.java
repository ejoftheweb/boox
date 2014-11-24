package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GWTDirectoryEntry implements Serializable {
 private String sysname;//of the enterprise to which this entry pertains.
 private String name;
 private String description;
 private String url;
 private Set<String> tags = new HashSet<String>();
 private String twitter;
 private String fb;
	public GWTDirectoryEntry() {
		// TODO Auto-generated constructor stub
	}
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Set<String> getTags() {
		return tags;
	}
	public void addTag(String tag) {
		this.tags.add(tag);
	}
	public String getTwitter() {
		return twitter;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	public String getFb() {
		return fb;
	}
	public void setFb(String fb) {
		this.fb = fb;
	}
	public void addTags(String taglist, String separator){
		String[] tags = taglist.split(separator);
		for(String tag:tags){
			addTag(tag);
		}
	}
	public void addTags(String taglist){
		String[] tags = taglist.split(",");
		for(String tag:tags){
			addTag(tag);
		}
	}
}
