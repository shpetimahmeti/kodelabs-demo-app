package org.kodelabs.domain.common.transaction;

import com.mongodb.reactivestreams.client.ClientSession;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mutiny.zero.flow.adapters.AdaptersToFlow;

@ApplicationScoped
public class TransactionService {

    @Inject
    ReactiveMongoClient mongoClient;

    public <T> Uni<T> inTransaction(TransactionCallback<T> callback) {
        return mongoClient.startSession()
                .onItem().transformToUni(session -> {
                    session.startTransaction();

                    return callback.apply(session)
                            .flatMap(result -> commitTx(session).replaceWith(result))
                            .onFailure().recoverWithUni(err ->
                                    abortTx(session).replaceWith(Uni.createFrom().failure(err))
                            )
                            .eventually(session::close);
                });
    }

    @FunctionalInterface
    public interface TransactionCallback<T> {
        Uni<T> apply(ClientSession session);
    }

    private Uni<Void> commitTx(ClientSession session) {
        return Uni.createFrom().publisher(
                AdaptersToFlow.publisher(session.commitTransaction())
        );
    }

    private Uni<Void> abortTx(ClientSession session) {
        return Uni.createFrom().publisher(
                AdaptersToFlow.publisher(session.abortTransaction())
        );
    }
}
