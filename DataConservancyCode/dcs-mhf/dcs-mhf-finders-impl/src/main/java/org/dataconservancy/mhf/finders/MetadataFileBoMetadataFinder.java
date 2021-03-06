/*
 * Copyright 2013 Johns Hopkins University
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
package org.dataconservancy.mhf.finders;


import org.dataconservancy.mhf.finder.api.MetadataFindingException;
import org.dataconservancy.mhf.model.builder.api.MetadataObjectBuilder;
import org.dataconservancy.mhf.representation.api.MetadataAttributeName;
import org.dataconservancy.mhf.representation.api.MetadataAttributeSetName;
import org.dataconservancy.mhf.representation.api.MetadataAttributeType;
import org.dataconservancy.mhf.representations.MetadataAttribute;
import org.dataconservancy.mhf.representations.MetadataAttributeSet;
import org.dataconservancy.ui.model.BusinessObject;
import org.dataconservancy.ui.model.MetadataFile;

/**
 * Responsible for finding Business Object metadata on a {@code org.dataconservancy.ui.model.MetadataFile}.
 * <p/>
 * This implementation looks for properties of the {@code MetadataFile}, such as its id, name, source, format, etc.  It
 * is <em>not</em> responsible for finding metadata that may be contained within the byte stream of the
 * {@code MetadataFile}.
 *
 * @see EmbeddedMetadataFinder
 */
public class MetadataFileBoMetadataFinder extends BusinessObjectMetadataFinder{

    public MetadataFileBoMetadataFinder(MetadataObjectBuilder builder) {
        super(builder);
    }

    @Override
    protected MetadataAttributeSet findCoreMetadata(BusinessObject bo) {
        System.out.println("Executing " + this.getClass());
        checkObjectType(bo);
        MetadataFile metadataFile = (MetadataFile) bo;
        final MetadataAttributeSet attributeSet = super.findCoreMetadata(metadataFile);
        attributeSet.setName(MetadataAttributeSetName.METADATA_CORE_METADATA);

        if (metadataFile.getName() != null) {
            attributeSet.addAttribute(new MetadataAttribute(MetadataAttributeName.TITLE, MetadataAttributeType.STRING, metadataFile.getName()));
        }

        if (metadataFile.getMetadataFormatId() != null) {
            attributeSet.addAttribute(new MetadataAttribute(MetadataAttributeName.FILE_FORMAT, MetadataAttributeType.STRING, metadataFile.getMetadataFormatId()));
        }

        return attributeSet;
    }

    @Override
    protected MetadataAttributeSet findSystemMetadata(BusinessObject bo) {
        return super.findSystemMetadata(bo);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void checkObjectType(BusinessObject o) {
        if (!(o instanceof MetadataFile)) {
            IllegalArgumentException iae = new IllegalArgumentException(
                    String.format(TYPE_ERROR, o.getClass().getName(), MetadataFile.class.getName()));
            throw new MetadataFindingException(iae.getMessage(), iae);
        }
    }
}
