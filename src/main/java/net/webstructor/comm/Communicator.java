/*
 * MIT License
 * 
 * Copyright (c) 2005-2019 by Anton Kolonin, Aigents
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.webstructor.comm;

import java.io.IOException;

import net.webstructor.agent.Body;
import net.webstructor.core.Anything;
import net.webstructor.peer.Session;

public abstract class Communicator extends Thread
{
	protected Body body;
	protected String name;
	
	private boolean bAlive;
	
	public Communicator(Body body,String name){
		this.body = body;
		this.name = name;
		bAlive = true;
	}

	public Communicator(Body body){
		this(body,null);
	}
	
	protected String key(String chat_id,String from_id){
		return (new StringBuilder(name == null ? "" : name).append(':').append(chat_id).append(':').append(from_id)).toString();
	}
	
	/**
	 * @param key of form name:chat_id:from_id
	 * @return array of name, chat_id, from_id 
	 */
	protected String[] ids(String key){
		String[] ids = key.split(":");
		return ids != null && ids.length == 3 ? ids: null;
	}
	
	public abstract void output(Session session, String message) throws IOException;

	public void terminate() {
		bAlive = false;
	}	

	public boolean alive() {
		return bAlive;
	}	
	
	public void login(Session session, Anything peer) {
		//pass by default
	}
}
