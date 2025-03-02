/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package group.rxcloud.cloudruntimes.domain.enhanced;

import group.rxcloud.cloudruntimes.domain.enhanced.database.CreateTableRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.database.CreateTableResponse;
import group.rxcloud.cloudruntimes.domain.enhanced.database.DeleteTableRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.database.DeleteTableResponse;
import group.rxcloud.cloudruntimes.domain.enhanced.database.GetConnectionRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.database.GetConnectionResponse;
import group.rxcloud.cloudruntimes.domain.enhanced.database.InsertRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.database.InsertResponse;
import group.rxcloud.cloudruntimes.domain.enhanced.database.QueryRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.database.QueryResponse;
import group.rxcloud.cloudruntimes.domain.enhanced.database.UpdateRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.database.UpdateResponse;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

/**
 * alpha API.
 * see https://github.com/dapr/dapr/issues/3354
 */
public interface DatabaseRuntimes {

    // -- Database Base Operation API

    /**
     * The Creating Connection maybe be inited by Component initialization，and the parameters of Creating Connection
     * comes from yaml metadata. But when we need to create multiple databases, we should create different database
     * connections. This is the reason why we need add Creating Connection API that the app can invoke many times to
     * create multiple connections.
     * If the app developer don't have requirement of multiple database connections, the app developer can ignore this
     * API but use the yaml file to init the database connection.
     */
    Mono<GetConnectionResponse> getConnection(GetConnectionRequest req);

    /**
     * Maybe dapr will be used in some application that is responsible for managing the database, so we will add the
     * creating table API to support it.
     */
    Mono<CreateTableResponse> createTable(CreateTableRequest req);

    /**
     * The reason need this api is the same as Create Table.
     */
    Mono<DeleteTableResponse> deleteTable(DeleteTableRequest req);

    // -- CURD API

    /**
     * The insert API with SQL
     */
    Mono<InsertResponse> insert(InsertRequest req);

    /**
     * The insert API with ORM
     */
    Mono<InsertResponse> insert(String dbName, String tableName, Object data);

    /**
     * The query API with SQL
     */
    <T> Mono<QueryResponse<T>> query(QueryRequest req, TypeRef<T> type);

    /**
     * The query API with ORM
     */
    <T> Mono<QueryResponse<T>> query(String dbName, String tableName, Object data, TypeRef<T> type);

    /**
     * The update API and delete API will be used with SQL.
     */
    Mono<UpdateResponse> update(UpdateRequest req);

    /**
     * The update API and delete API will be used with ORM.
     */
    Mono<UpdateResponse> update(String dbName, String tableName, Object data);

    // -- Database Transaction Operation API

    Mono<Void> BeginTransaction();

    Mono<Void> UpdateTransaction();

    Mono<Void> QueryTransaction();

    Mono<Void> CommitTransaction();

    Mono<Void> RollbackTransaction();
}
