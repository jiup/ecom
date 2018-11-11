/*
 * Copyright 2018 Jiupeng Zhang
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

package com.codeager.ecom.service.impl;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.codeager.ecom.service.ImageUploadService;
import com.codeager.plugin.ImageUploader;
import com.codeager.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.Executors;

/**
 * @author Jiupeng Zhang
 * @since 01/08/2018
 */
@Service
public class ImageUploadServiceImpl extends Plugin<ImageUploader> implements ImageUploadService {
    protected static final Logger LOG = LoggerFactory.getLogger(ImageUploadService.class);
    protected static final ListeningExecutorService IMAGE_UPLOAD_SERVICE_EXECUTOR;

    static {
        IMAGE_UPLOAD_SERVICE_EXECUTOR = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    }

    @Autowired
    protected ImageUploadServiceImpl(Optional<ImageUploader> optionalFutureHolder) {
        super(optionalFutureHolder);
    }

    @Override
    public String upload(InputStream imageFileStream) {
        String ret = null;

        try {
            ret = futureHolder.upload(imageFileStream);
            LOG.info("Image file was successfully uploaded to {}", ret);
        } catch (Exception e) {
            LOG.error("Image file upload error: {}", e.getMessage());
        }

        return ret;
    }
}
