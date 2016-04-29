package com.threecore.project.operator.source.test;

import com.threecore.project.model.Post;

@Deprecated
public class PostSource extends BaseFileSource<Post> {

	private static final long serialVersionUID = 1L;

	public PostSource(final String dataPath) {
		super(dataPath);
	}

	@Override
	public Post parseElement(String line) {
		Post post = Post.fromString(line);
		return post;
	}
	
	@Override
	public void doSomethingWithElementBeforeEmitting(Post post) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getElementTimestampMillis(Post post) {
		return post.getTimestamp();
	}	

}
