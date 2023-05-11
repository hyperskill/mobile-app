package org.hyperskill.app.android.core.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.chrynan.parcelable.core.getParcelable
import com.chrynan.parcelable.core.putParcelable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlinx.serialization.KSerializer

@Suppress("UnusedReceiverParameter")
fun <T : Any> Fragment.argument(serializer: KSerializer<T>): ReadWriteProperty<Fragment, T> =
    FragmentSerializableArgumentDelegate(serializer)

/**
 * Delegate to access/write complex objects stored in Fragment.arguments.
 */
class FragmentSerializableArgumentDelegate<T : Any>(serializer: KSerializer<T>) : ReadWriteProperty<Fragment, T> {

    private val bundleDelegate = BundleDelegate(serializer)

    override operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val args = thisRef.arguments
            ?: throw IllegalStateException("Cannot read property ${property.name} if no arguments have been set")
        return bundleDelegate.getValue(args, property)
    }

    override operator fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        val args = thisRef.arguments ?: Bundle().also(thisRef::setArguments)
        bundleDelegate.setValue(args, property, value)
    }
}

class BundleDelegate<T : Any>(private val serializer: KSerializer<T>) : ReadWriteProperty<Bundle, T> {

    private var value: T? = null

    override operator fun getValue(thisRef: Bundle, property: KProperty<*>): T {
        if (value == null) {
            value = thisRef.getParcelable(property.name, deserializer = serializer)
        }
        return value ?: throw IllegalStateException("Property ${property.name} could not be read")
    }

    override operator fun setValue(thisRef: Bundle, property: KProperty<*>, value: T) {
        val key = property.name
        thisRef.putParcelable(key, value, serializer = serializer)
        this.value = value
    }
}