package org.wordpress.android.fluxc.persistence

import android.annotation.SuppressLint
import com.wellsql.generated.NotificationModelTable
import com.yarolegovich.wellsql.SelectQuery
import com.yarolegovich.wellsql.SelectQuery.ORDER_DESCENDING
import com.yarolegovich.wellsql.WellSql
import com.yarolegovich.wellsql.core.Identifiable
import com.yarolegovich.wellsql.core.annotation.Column
import com.yarolegovich.wellsql.core.annotation.PrimaryKey
import com.yarolegovich.wellsql.core.annotation.Table
import org.wordpress.android.fluxc.model.NotificationModel
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.tools.FormattableContent
import org.wordpress.android.fluxc.tools.FormattableContentMapper
import org.wordpress.android.fluxc.tools.FormattableMeta
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationSqlUtils @Inject constructor(private val formattableContentMapper: FormattableContentMapper) {
    fun insertOrUpdateNotification(notification: NotificationModel): Int {
        val notificationResult = WellSql.select(NotificationModelBuilder::class.java)
                .where().beginGroup()
                .equals(NotificationModelTable.ID, notification.noteId)
                .or()
                .beginGroup()
                .equals(NotificationModelTable.REMOTE_SITE_ID, notification.remoteSiteId)
                .equals(NotificationModelTable.REMOTE_NOTE_ID, notification.remoteNoteId)
                .endGroup()
                .endGroup().endWhere()
                .asModel

        return if (notificationResult.isEmpty()) {
            // insert
            WellSql.insert(notification.toBuilder()).asSingleTransaction(true).execute()
            1
        } else {
            // update
            val oldId = notificationResult[0].id
            WellSql.update(NotificationModelBuilder::class.java).whereId(oldId).put(
                    notification.toBuilder(),
                    UpdateAllExceptId<NotificationModelBuilder>(NotificationModelBuilder::class.java)
            ).execute()
        }
    }

    @SuppressLint("WrongConstant")
    fun getNotifications(
        @SelectQuery.Order order: Int = ORDER_DESCENDING,
        filterByType: List<String>? = null,
        filterBySubtype: List<String>? = null
    ): List<NotificationModel> {
        val conditionClauseBuilder = WellSql.select(NotificationModelBuilder::class.java)
                .where()

        if (filterByType != null || filterBySubtype != null) {
            conditionClauseBuilder.beginGroup()

            filterByType?.let {
                conditionClauseBuilder.isIn(NotificationModelTable.TYPE, it)
            }

            if (filterByType != null && filterBySubtype != null) {
                conditionClauseBuilder.or()
            }

            filterBySubtype?.let {
                conditionClauseBuilder.isIn(NotificationModelTable.SUBTYPE, it)
            }

            conditionClauseBuilder.endGroup()
        }

        return conditionClauseBuilder.endWhere()
                .orderBy(NotificationModelTable.TIMESTAMP, order)
                .asModel
                .map { it.build(formattableContentMapper) }
        return emptyList()
    }

    @SuppressLint("WrongConstant")
    fun getNotificationsForSite(
        site: SiteModel,
        @SelectQuery.Order order: Int = ORDER_DESCENDING,
        filterByType: List<String>? = null,
        filterBySubtype: List<String>? = null
    ): List<NotificationModel> {
        val conditionClauseBuilder = WellSql.select(NotificationModelBuilder::class.java)
                .where()
                .equals(NotificationModelTable.REMOTE_SITE_ID, site.id)

        if (filterByType != null || filterBySubtype != null) {
            conditionClauseBuilder.beginGroup()

            filterByType?.let {
                conditionClauseBuilder.isIn(NotificationModelTable.TYPE, it)
            }

            if (filterByType != null && filterBySubtype != null) {
                conditionClauseBuilder.or()
            }

            filterBySubtype?.let {
                conditionClauseBuilder.isIn(NotificationModelTable.SUBTYPE, it)
            }

            conditionClauseBuilder.endGroup()
        }

        return conditionClauseBuilder.endWhere()
                .orderBy(NotificationModelTable.TIMESTAMP, order)
                .asModel
                .map { it.build(formattableContentMapper) }
        return emptyList()
    }

    fun deleteNotifications(): Int {
        return WellSql.delete(NotificationModelBuilder::class.java).execute()
    }

    private fun NotificationModel.toBuilder(): NotificationModelBuilder {
        return NotificationModelBuilder(
                mId = this.noteId,
                remoteNoteId = this.remoteNoteId,
                remoteSiteId = this.remoteSiteId,
                noteHash = this.noteHash,
                type = this.type.toString(),
                subtype = this.subtype.toString(),
                read = this.read,
                icon = this.icon,
                noticon = this.noticon,
                timestamp = this.timestamp,
                url = this.url,
                title = this.title,
                formattableBody = this.body?.let { formattableContentMapper.mapFormattableContentListToJson(it) },
                formattableSubject = this.subject?.let { formattableContentMapper.mapFormattableContentListToJson(it) },
                formattableMeta = this.meta?.let { formattableContentMapper.mapFormattableMetaToJson(it) }
        )
    }

    @Table(name = "NotificationModel")
    data class NotificationModelBuilder(
        @PrimaryKey @Column private var mId: Int = -1,
        @Column var remoteNoteId: Long,
        @Column var remoteSiteId: Long?,
        @Column var noteHash: Long,
        @Column var type: String,
        @Column var subtype: String? = null,
        @Column var read: Boolean = false,
        @Column var icon: String? = null,
        @Column var noticon: String? = null,
        @Column var timestamp: String? = null,
        @Column var url: String? = null,
        @Column var title: String? = null,
        @Column var formattableBody: String? = null,
        @Column var formattableSubject: String? = null,
        @Column var formattableMeta: String? = null
    ) : Identifiable {
        constructor() : this(-1, 0L, 0, 0L, NotificationModel.Kind.STORE_ORDER.toString())
        override fun setId(id: Int) {
            this.mId = id
        }

        override fun getId() = this.mId

        fun build(formattableContentMapper: FormattableContentMapper): NotificationModel {
            val subkind: NotificationModel.Subkind? = subtype?.let { NotificationModel.Subkind.fromString(it) }

            val body: List<FormattableContent>? = formattableBody?.let {
                formattableContentMapper.mapToFormattableContentList(it)
            }
            val subject: List<FormattableContent>? = formattableSubject?.let {
                formattableContentMapper.mapToFormattableContentList(it)
            }
            val meta: FormattableMeta? = formattableMeta?.let {
                formattableContentMapper.mapToFormattableMeta(it)
            }
            return NotificationModel(
                    mId,
                    remoteNoteId,
                    remoteSiteId,
                    noteHash,
                    NotificationModel.Kind.fromString(type),
                    subkind,
                    read,
                    icon,
                    noticon,
                    timestamp,
                    url,
                    title,
                    body,
                    subject,
                    meta)
        }
    }
}
