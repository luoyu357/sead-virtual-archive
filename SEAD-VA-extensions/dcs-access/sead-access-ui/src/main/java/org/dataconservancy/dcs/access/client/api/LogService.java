/*
 * Copyright 2013 The Trustees of Indiana University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dataconservancy.dcs.access.client.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dataconservancy.dcs.access.client.model.CollectionNode;
import org.dataconservancy.dcs.access.client.model.DatasetRelation;
import org.dataconservancy.dcs.access.client.model.FileNode;
import org.dataconservancy.dcs.access.client.upload.RPCException;
import org.dataconservancy.dcs.access.client.upload.model.Package;
import org.dataconservancy.dcs.access.shared.CheckPointDetail;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("log")
public interface LogService extends RemoteService {
	
	void writeLog(String content);
	}
