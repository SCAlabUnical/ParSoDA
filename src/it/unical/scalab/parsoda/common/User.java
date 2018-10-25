package it.unical.scalab.parsoda.common;

import java.util.HashMap;

public class User extends HashMap<String, String> {

	private static final long serialVersionUID = 1L;

	public User(String id, HashMap<String, String> metadata) {
		this.putAll(metadata);
		this.put(Metadata.USERID, id);
	}

	public User() {
	}

	public User(String id) {
		this.put(Metadata.USERID, id);
	}

	public String getId() {
		return get(Metadata.USERID);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

}
