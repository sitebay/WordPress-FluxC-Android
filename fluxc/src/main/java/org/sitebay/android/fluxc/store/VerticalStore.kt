package org.sitebay.android.fluxc.store

import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.sitebay.android.fluxc.Dispatcher
import org.sitebay.android.fluxc.Payload
import org.sitebay.android.fluxc.action.VerticalAction
import org.sitebay.android.fluxc.annotations.action.Action
import org.sitebay.android.fluxc.model.vertical.VerticalSegmentModel
import org.sitebay.android.fluxc.network.rest.wpcom.vertical.VerticalRestClient
import org.sitebay.android.fluxc.tools.CoroutineEngine
import org.sitebay.android.util.AppLog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VerticalStore @Inject constructor(
    private val verticalRestClient: VerticalRestClient,
    private val coroutineEngine: CoroutineEngine,
    dispatcher: Dispatcher
) : Store(dispatcher) {
    @Subscribe(threadMode = ThreadMode.ASYNC)
    override fun onAction(action: Action<*>) {
        val actionType = action.type as? VerticalAction ?: return

        when (actionType) {
            VerticalAction.FETCH_SEGMENTS -> {
                coroutineEngine.launch(AppLog.T.API, this, "FETCH_SEGMENTS") {
                    emitChange(fetchSegments())
                }
            }
        }
    }

    override fun onRegister() {
        AppLog.d(AppLog.T.API, VerticalStore::class.java.simpleName + " onRegister")
    }

    private suspend fun fetchSegments(): OnSegmentsFetched {
        val fetchedSegmentsPayload = verticalRestClient.fetchSegments()
        return OnSegmentsFetched(fetchedSegmentsPayload.segmentList, fetchedSegmentsPayload.error)
    }

    class OnSegmentsFetched(
        val segmentList: List<VerticalSegmentModel>,
        error: FetchSegmentsError? = null
    ) : Store.OnChanged<FetchSegmentsError>() {
        init {
            this.error = error
        }
    }

    class FetchSegmentPromptPayload(val segmentId: Long)

    class FetchedSegmentsPayload(val segmentList: List<VerticalSegmentModel>) : Payload<FetchSegmentsError>() {
        constructor(error: FetchSegmentsError) : this(emptyList()) {
            this.error = error
        }
    }

    class FetchSegmentsError(val type: VerticalErrorType, val message: String? = null) : Store.OnChangedError
    enum class VerticalErrorType {
        GENERIC_ERROR
    }
}
