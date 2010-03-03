package com.aptana.git.core.model;

import java.util.regex.Pattern;

/**
 * Represents a reference in the git repo. Typically branches or tags.
 * 
 * @author cwilliams
 */
public class GitRef
{

	/**
	 * Ref dirs
	 */
	static final String REFS = "refs/"; //$NON-NLS-1$
	static final String REFS_REMOTES = REFS + "remotes/"; //$NON-NLS-1$
	static final String REFS_TAGS = REFS + "tags/"; //$NON-NLS-1$
	static final String REFS_HEADS = REFS + "heads/"; //$NON-NLS-1$

	/**
	 * Ref Types
	 */
	public enum TYPE
	{
		TAG, HEAD, REMOTE
	}

	private String ref;

	private GitRef(String string)
	{
		this.ref = string;
	}

	static GitRef refFromString(String string)
	{
		return new GitRef(string);
	}

	/**
	 * The full name of the ref. i.e. "refs/heads/master" or "refs/tags/v0.7"
	 * 
	 * @return
	 */
	public String ref()
	{
		return ref;
	}

	/**
	 * Short name for the ref. i.e. "master" or "v0.7"
	 * 
	 * @return
	 */
	public String shortName()
	{
		TYPE type = type();
		if (type == null)
			return ref;
		switch (type)
		{
			case REMOTE:
				return ref.substring(REFS_REMOTES.length());
			case HEAD:
				return ref.substring(REFS_HEADS.length());
			case TAG:
				return ref.substring(REFS_TAGS.length());
			default:
				return ref;
		}
	}

	/**
	 * Type of reference. head (local branch), remote (remote branch) or tag.
	 * 
	 * @return
	 */
	public TYPE type()
	{
		if (ref.startsWith(REFS_HEADS))
			return TYPE.HEAD;
		if (ref.startsWith(REFS_TAGS))
			return TYPE.TAG;
		if (ref.startsWith(REFS_REMOTES))
			return TYPE.REMOTE;
		return null;
	}

	@Override
	public String toString()
	{
		return ref;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof GitRef))
			return false;
		GitRef other = (GitRef) obj;
		return toString().equals(other.toString());
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	public String getRemoteName()
	{
		if (type() != TYPE.REMOTE)
			return null;
		String shortName = shortName();
		return shortName.split(Pattern.quote("/"))[0]; //$NON-NLS-1$
	}

	public String getRemoteBranchName()
	{
		if (type() != TYPE.REMOTE)
			return null;
		String shortName = shortName();
		return shortName.split(Pattern.quote("/"))[1]; //$NON-NLS-1$
	}
}
