package com.yap.yappk.pk.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.pk.SessionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PhoneContactsProvider @Inject constructor(
    @ApplicationContext val appContext: Context,
    private val sessionManager: SessionManager
) {

    fun getLocalContacts() = fetchContacts(appContext)

    private fun fetchContacts(context: Context): MutableList<Contact> {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val contacts = LinkedHashMap<Long, Contact>()

            val projection = arrayOf(
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DISPLAY_NAME_PRIMARY,
                ContactsContract.Data.PHOTO_THUMBNAIL_URI,
                ContactsContract.Data.DATA1,
                ContactsContract.Data.MIMETYPE
            )
            val cursor = context.contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                generateSelection(), null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " ASC"
            )

            if (cursor != null) {
                cursor.moveToFirst()
                val idColumnIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)
                val displayNamePrimaryColumnIndex =
                    cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY)
                val thumbnailColumnIndex =
                    cursor.getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI)
                val mimetypeColumnIndex = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE)
                val dataColumnIndex = cursor.getColumnIndex(ContactsContract.Data.DATA1)
                while (!cursor.isAfterLast) {
                    val id = cursor.getLong(idColumnIndex)
                    var contact = contacts[id]
                    if (contact == null) {

                        contact = Contact()
                        val displayName = cursor.getString(displayNamePrimaryColumnIndex)
                        if (displayName != null && displayName.isNotEmpty()) {
                            contact.title = displayName
                        }
                        mapThumbnail(cursor, contact, thumbnailColumnIndex)
                        contacts[id] = contact
                    }
                    val mimetype = cursor.getString(mimetypeColumnIndex)
                    when (mimetype) {
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> mapEmail(
                            cursor,
                            contact,
                            dataColumnIndex
                        )
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> {
                            var phoneNumber: String? = cursor.getString(dataColumnIndex)
                            if (phoneNumber != null && phoneNumber.isNotEmpty()) {
                                phoneNumber = phoneNumber.replace("\\s+".toRegex(), "")

                                try {
                                    val pn =
                                        PhoneNumberUtil.getInstance().parse(
                                            phoneNumber,
                                            sessionManager.userAccount.value?.currentCustomer?.getDefaultCountryCode(
                                                context
                                            )
                                        )
                                    contact.mobileNo = pn.nationalNumber.toString()
                                    contact.countryCode = "00${pn.countryCode}"
                                } catch (e: Exception) {
                                    Log.e("PhoneContactsProvider", e.localizedMessage)
                                }
                            }
                        }
                    }
                    cursor.moveToNext()
                }
                cursor.close()
            }
            return removeOwnContact(
                ArrayList(contacts.values),
                sessionManager.userAccount.value?.currentCustomer?.mobileNo ?: ""
            )
        }
        return mutableListOf()
    }

    private fun generateSelection(): String {
        val mSelectionBuilder = StringBuilder()
        if (mSelectionBuilder.isNotEmpty())
            mSelectionBuilder.append(" AND ")
        mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER)
            .append(" = 1")
        return mSelectionBuilder.toString()
    }

    private fun mapThumbnail(cursor: Cursor, contact: Contact, columnIndex: Int) {
        val uri = cursor.getString(columnIndex)
        if (uri != null && uri.isNotEmpty()) {
            contact.beneficiaryPictureUrl = uri
        }
    }

    private fun mapEmail(cursor: Cursor, contact: Contact, columnIndex: Int) {
        val email = cursor.getString(columnIndex)
        if (email != null && email.isNotEmpty()) {
            contact.email = email
        }
    }

    fun removeOwnContact(
        contacts: MutableList<Contact>,
        userMobileNo: String
    ): MutableList<Contact> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contacts.removeIf { it.mobileNo == userMobileNo }
        } else {
            contacts.remove(contacts.find { it.mobileNo == userMobileNo })
        }

        return contacts
    }
}

