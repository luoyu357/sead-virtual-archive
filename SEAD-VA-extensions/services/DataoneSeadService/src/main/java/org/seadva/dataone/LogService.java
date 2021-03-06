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

package org.seadva.dataone;

import org.dataconservancy.dcs.query.api.QueryServiceException;
import org.dataone.service.types.v1.*;
import org.jibx.runtime.JiBXException;
import org.seadva.model.SeadEvent;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.TransformerException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/*
 * Retrieves logs from DataONE Reads
 */

@Path("/mn/v1/log")
public class LogService{
    public LogService() {
    }

    @Produces(MediaType.APPLICATION_XML)
    @GET
    public String getLogRecords(@QueryParam("start") int start,
                             @QueryParam("count") String countStr,
                             @QueryParam("event") String event,
                             @QueryParam("pidFilter") String pidFilter,
                             @QueryParam("fromDate") String fromDate,
                             @QueryParam("toDate") String toDate) throws QueryServiceException, DatatypeConfigurationException, JiBXException, ParseException, TransformerException {


        Log log = new Log();

        DataOneLogService.Result result = SeadQueryService.dataOneLogService.queryLog(start,countStr,event, pidFilter,fromDate, toDate);

        for(SeadEvent d1log: result.logs){

            if(d1log.getLogDetail().getSubject() == null || d1log.getLogDetail().getNodeIdentifier() == null) {
                continue;
            }

            LogEntry logEntry = new LogEntry();

            logEntry.setEntryId(d1log.getId());
            Event eventType = Event.convert(
                    SeadQueryService.sead2d1EventTypes.get(d1log.getEventType()));
            if(eventType == Event.READ)
                eventType = Event.READ;
            logEntry.setEvent(
                       eventType
            );

            Identifier identifier = new Identifier();
            identifier.setValue(d1log.getId()); //DcsEvent Identifier
            logEntry.setIdentifier(identifier);
            String ipaddress = d1log.getLogDetail().getIpAddress();

            if(ipaddress==null)
                ipaddress="N/A";

            logEntry.setIpAddress(ipaddress);

            String date = d1log.getDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            logEntry.setDateLogged(simpleDateFormat.parse(date));



            String userAgent = d1log.getLogDetail().getUserAgent();
            if(userAgent==null)
                userAgent= "N/A";
            logEntry.setUserAgent(userAgent);
            Subject subject = new Subject();
            subject.setValue(d1log.getLogDetail().getSubject());
            logEntry.setSubject(subject);
            NodeReference nodeReference = new NodeReference();
            nodeReference.setValue(d1log.getLogDetail().getNodeIdentifier());
            logEntry.setNodeIdentifier(nodeReference);
            log.getLogEntryList().add(logEntry);
        }

        log.setCount(result.logs.size());
        log.setTotal((int)result.total);
        log.setStart(start);
        return SeadQueryService.marshal(log);
    }
}
