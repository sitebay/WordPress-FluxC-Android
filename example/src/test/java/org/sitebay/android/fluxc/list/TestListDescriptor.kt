package org.sitebay.android.fluxc.list

import com.nhaarman.mockitokotlin2.mock
import org.sitebay.android.fluxc.model.list.ListConfig
import org.sitebay.android.fluxc.model.list.ListDescriptor
import org.sitebay.android.fluxc.model.list.ListDescriptorTypeIdentifier
import org.sitebay.android.fluxc.model.list.ListDescriptorUniqueIdentifier
import org.sitebay.android.fluxc.model.list.datasource.InternalPagedListDataSource
import org.sitebay.android.fluxc.model.list.datasource.ListItemDataSourceInterface

internal typealias TestListIdentifier = Long
internal typealias TestPagedListResultType = String
internal typealias TestInternalPagedListDataSource =
        InternalPagedListDataSource<TestListDescriptor, TestListIdentifier, TestPagedListResultType>
internal typealias TestListItemDataSource =
        ListItemDataSourceInterface<TestListDescriptor, TestListIdentifier, TestPagedListResultType>

internal class TestListDescriptor(
    override val uniqueIdentifier: ListDescriptorUniqueIdentifier = mock(),
    override val typeIdentifier: ListDescriptorTypeIdentifier = mock(),
    override val config: ListConfig = ListConfig.default
) : ListDescriptor
