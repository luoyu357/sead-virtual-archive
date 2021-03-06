/*
 * Copyright 2012 Johns Hopkins University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dataconservancy.dcs.access.impl.solr;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.solr.common.util.DateUtil;

import org.dataconservancy.dcs.util.DateUtility;
import org.dataconservancy.model.dcs.DcsCollection;
import org.dataconservancy.model.dcs.DcsCollectionRef;
import org.dataconservancy.model.dcs.DcsDeliverableUnit;
import org.dataconservancy.model.dcs.DcsDeliverableUnitRef;
import org.dataconservancy.model.dcs.DcsEntityReference;
import org.dataconservancy.model.dcs.DcsEvent;
import org.dataconservancy.model.dcs.DcsFile;
import org.dataconservancy.model.dcs.DcsFileRef;
import org.dataconservancy.model.dcs.DcsFixity;
import org.dataconservancy.model.dcs.DcsFormat;
import org.dataconservancy.model.dcs.DcsManifestation;
import org.dataconservancy.model.dcs.DcsManifestationFile;
import org.dataconservancy.model.dcs.DcsMetadata;
import org.dataconservancy.model.dcs.DcsMetadataRef;
import org.dataconservancy.model.dcs.DcsRelation;

/**
 * See {@link org.dataconservancy.dcs.index.dcpsolr.RandomEntityBuilder}
 */

@Deprecated
public class RandomEntityBuilder {

    private final Random rand;

    private int nextid;

    public RandomEntityBuilder() {
        this.nextid = 0;
        this.rand = new Random();
    }

    public RandomEntityBuilder(long seed) {
        this.nextid = 0;
        this.rand = new Random(seed);
    }

    public DcsEvent createEvent() {
        DcsEvent event = new DcsEvent();

        event.setId(nextid());
        event.setDate(DateUtil.getThreadLocalDateFormat().format(new Date()));
        event.setOutcome(randomString(20, true));
        event.setEventType(randomString(2, true));
        event.setDetail(randomText(5));
        event.setTargets(createEnityReferences(rand.nextInt(2)));

        return event;
    }

    private Set<DcsEntityReference> createEnityReferences(int size) {
        Set<DcsEntityReference> set = new HashSet<DcsEntityReference>();

        for (int i = 0; i < size; i++) {
            DcsEntityReference ref = new DcsEntityReference();
            ref.setRef(randomString(3, false));
        }

        return set;
    }

    public DcsManifestation createManifestation(String du, List<DcsFile> files) {
        DcsManifestation man = new DcsManifestation();

        man.setId(nextid());
        man.setDeliverableUnit(du);
        man.setDateCreated(DateUtility.toIso8601(DateUtility.now()));
        man.setMetadata(createMetadataSet(rand.nextInt(3)));
        man.setMetadataRef(createMetadataRefSet(rand.nextInt(2)));
        
        int numfiles = rand.nextInt(10);

        for (int i = 0; i < numfiles; i++) {
            DcsManifestationFile manfile = new DcsManifestationFile();
            DcsFile file = createFile();

            files.add(file);

            manfile.setRef(new DcsFileRef(file.getId()));
            manfile.setPath(randomString(10, false));

            man.addManifestationFile(manfile);
        }

        return man;
    }

    public DcsFile createFile() {
        DcsFile file = new DcsFile();

        file.setExtant(rand.nextBoolean());
        file.setId(nextid());
        file.setFixity(createFixitySet(rand.nextInt(3)));
        file.setFormats(createFormatSet(rand.nextInt(2)));
        file.setName(randomString(8, true));
        file.setSizeBytes(Math.abs(rand.nextLong()));
        file.setSource(randomString(10, true));

        if (rand.nextBoolean()) {
            file.setValid(rand.nextBoolean());
        }

        file.setMetadata(createMetadataSet(rand.nextInt(2)));
        file.setMetadataRef(createMetadataRefSet(rand.nextInt(2)));

        return file;
    }

    private Set<DcsFormat> createFormatSet(int size) {
        Set<DcsFormat> set = new HashSet<DcsFormat>();

        for (int i = 0; i < size; i++) {
            DcsFormat fmt = new DcsFormat();

            fmt.setFormat(randomString(10, true));
            fmt.setName(randomString(10, true));
            fmt.setSchemeUri(randomString(10, true));
            fmt.setVersion(randomString(10, true));

            set.add(fmt);
        }

        return set;
    }

    private Set<DcsFixity> createFixitySet(int size) {
        Set<DcsFixity> set = new HashSet<DcsFixity>();

        for (int i = 0; i < size; i++) {
            DcsFixity fix = new DcsFixity();

            fix.setAlgorithm(randomString(10, true));
            fix.setValue(randomString(10, true));

            set.add(fix);
        }

        return set;
    }

    private String nextid() {
        return "http://test.dataconservancy.org/" + nextid++;

    }

    private String randomString(int length, boolean maybenull) {
        if (maybenull && rand.nextBoolean()) {
            return null;
        }

        char[] buf = new char[length];

        for (int i = 0; i < length; i++) {
            buf[i] = (char) ('a' + rand.nextInt(26));
        }

        return new String(buf);
    }

    private String randomXML(int elements) {
        StringBuilder sb = new StringBuilder();

        sb.append("<root>");

        for (int i = 0; i < elements; i++) {
            String el = "el" + randomString(2, false);

            sb.append("<" + el + ">" + randomString(2, false) + "</" + el
                    + ">");

            sb.append("<" + el + " " + randomString(2, false) + "='"
                    + randomString(5, false) + "'>" + randomString(10, false)
                    + "</" + el + ">");
        }

        sb.append("</root>");

        return sb.toString();
    }

    private String randomText(int words) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < words; i++) {
            sb.append(randomString(10, false));

            if (i != words - 1) {
                sb.append(' ');
            }
        }

        return sb.toString();
    }

    private Set<String> randomStringSet(int size, int strlength) {
        Set<String> result = new HashSet<String>();

        for (int i = 0; i < size; i++) {
            result.add(randomString(strlength, false));
        }

        return result;
    }

    private Set<DcsMetadataRef> createMetadataRefSet(int size) {
        Set<DcsMetadataRef> result = new HashSet<DcsMetadataRef>();

        for (int i = 0; i < size; i++) {
            DcsMetadataRef ref = new DcsMetadataRef();

            ref.setRef(randomString(10, false));

            result.add(ref);
        }

        return result;
    }

    private Set<DcsCollectionRef> createCollectionRefSet(String string) {
        Set<DcsCollectionRef> result = new HashSet<DcsCollectionRef>();

        DcsCollectionRef ref = new DcsCollectionRef();
        ref.setRef(string);
        result.add(ref);

        return result;
    }

    private DcsCollectionRef createCollectionRef(String string) {
        DcsCollectionRef ref = new DcsCollectionRef();
        ref.setRef(string);

        return ref;
    }

    private Set<DcsMetadata> createMetadataSet(int size) {
        Set<DcsMetadata> result = new HashSet<DcsMetadata>();

        for (int i = 0; i < size; i++) {
            result.add(createMetaData());
        }

        return result;
    }

    public DcsDeliverableUnit createDeliverableUnit(String collection) {
        DcsDeliverableUnit du = new DcsDeliverableUnit();

        du.setId(nextid());
        du.setTitle(randomString(rand.nextInt(10) + 1, false));
        du.setCreators(randomStringSet(rand.nextInt(2) + 1,
                                       rand.nextInt(10) + 1));
        du
                .setSubjects(randomStringSet(rand.nextInt(2) + 1, rand
                        .nextInt(5) + 1));

        du.setMetadata(createMetadataSet(rand.nextInt(2)));
        du.setMetadataRef(createMetadataRefSet(rand.nextInt(3)));
        du.setFormerExternalRefs(randomStringSet(rand.nextInt(5), 10));

        if (collection != null) {
            du.setCollections(createCollectionRefSet(collection));
        }

        du.setRelations(createRelations(rand.nextInt(2)));
        du.setType(randomString(4, false));

        if (rand.nextBoolean()) {
            du.setDigitalSurrogate(rand.nextBoolean());
        }

        du.setParents(createDeliverableUnitRefSet(rand.nextInt(2)));

        return du;
    }

    private Set<DcsDeliverableUnitRef> createDeliverableUnitRefSet(int size) {
        Set<DcsDeliverableUnitRef> set = new HashSet<DcsDeliverableUnitRef>();

        for (int i = 0; i < size; i++) {
            set.add(new DcsDeliverableUnitRef(randomString(3, false)));
        }

        return set;
    }

    private Set<DcsRelation> createRelations(int size) {
        Set<DcsRelation> set = new HashSet<DcsRelation>();

        for (int i = 0; i < size; i++) {
            DcsRelation rel = new DcsRelation();

            rel.setRef(new DcsEntityReference(randomString(10, false)));
            rel.setRelUri(randomString(10, false));

            set.add(rel);
        }

        return set;
    }

    private DcsMetadata createMetaData() {
        DcsMetadata md = new DcsMetadata();

        md.setSchemaUri(randomString(10, true));
        md.setMetadata(randomXML(3));

        return md;
    }

    public DcsCollection createCollection(String parent) {
        DcsCollection col = new DcsCollection();

        col.setId(nextid());
        col.setTitle(randomText(rand.nextInt(10) + 1));
        if (parent != null) {
            col.setParent(createCollectionRef(parent));
        }

        col.setMetadata(createMetadataSet(rand.nextInt(2)));
        col.setSubjects(randomStringSet(4, 4));
        col.setType(randomString(4, false));
        col.setCreators(randomStringSet(4, 4));

        return col;
    }
}
