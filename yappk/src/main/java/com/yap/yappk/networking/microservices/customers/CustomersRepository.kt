package com.yap.yappk.networking.microservices.customers

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.base.BaseRepository
import com.yap.yappk.networking.apiclient.base.CookiesManager
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.authentication.requestdtos.DemographicDataRequest
import com.yap.yappk.networking.microservices.customers.requestsdtos.*
import com.yap.yappk.networking.microservices.customers.responsedtos.*
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.networking.microservices.customers.responsedtos.documents.Documents
import com.yap.yappk.networking.microservices.customers.responsedtos.documents.IDCardDetect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CustomersRepository @Inject constructor(
    private val service: CustomersRetroService
) : BaseRepository(), CustomersApi {

    override suspend fun emailVerification(emailVerificationRequest: EmailVerificationRequest): ApiResponse<BaseResponse<String>> =
        executeSafely(call = { service.emailVerification(emailVerificationRequest) })

    override suspend fun signUp(signUpRequest: SignUpRequest): ApiResponse<BaseResponse<String>> {
        val response = executeSafely(call = { service.signUp(signUpRequest) })
        when (response) {
            is ApiResponse.Success -> {
                CookiesManager.jwtToken = response.data.data
                CookiesManager.isLoggedIn = true
            }
            else -> {
                // to be handle later
            }
        }
        return response
    }

    override suspend fun postDemographicData(demographicDataRequest: DemographicDataRequest): ApiResponse<BaseApiResponse> =
        executeSafely(call = { service.postDemographicData(demographicDataRequest) })

    override suspend fun getAccountInfo(): ApiResponse<BaseListResponse<AccountInfo>> =
        executeSafely(call = { service.getAccountInfo() })

    override suspend fun getWaitingRanking(): ApiResponse<BaseResponse<WaitingListResponse>> =
        executeSafely(call = { service.getWaitingRanking() })

    override suspend fun saveReferralInvitation(saveReferralRequest: SaveReferralRequest): ApiResponse<BaseApiResponse> =
        executeSafely(call = { service.saveReferralInvitation(saveReferralRequest) })

    override suspend fun sendInviteFriend(sendInviteFriendRequest: SendInviteFriendRequest): ApiResponse<BaseApiResponse> =
        executeSafely(call = { service.sendInviteFriend(sendInviteFriendRequest) })

    override suspend fun verifyUser(user: String): ApiResponse<BaseResponse<Boolean>> =
        executeSafely(call = { service.verifyUser(user) })

    override suspend fun generateOTPForDeviceVerification(demographicDataRequest: DemographicDataRequest): ApiResponse<BaseApiResponse> =
        executeSafely(call = { service.generateOTPForDeviceVerification(demographicDataRequest) })

    override suspend fun verifyOTPForDeviceVerification(demographicDataRequest: DemographicDataRequest): ApiResponse<BaseResponse<String>> =
        executeSafely(call = { service.verifyOTPForDeviceVerification(demographicDataRequest) })

    override suspend fun completeVerification(completeVerificationRequest: CompleteVerificationRequest): ApiResponse<BaseApiResponse> =
        executeSafely(call = { service.completeVerification(completeVerificationRequest) })

    override suspend fun getDocumentsByType(documentType: String): ApiResponse<BaseResponse<Documents>> =
        executeSafely(call = { service.getDocumentsByType(documentType) })

    override suspend fun getDocumentDetail(documentDetailRequest: DocumentDetailRequest): ApiResponse<BaseResponse<DocumentDetails>> =
        executeSafely(call = { service.getDocumentDetails(documentDetailRequest) })

    override suspend fun uploadDocument(uploadDocumentsRequest: UploadDocumentsRequest): ApiResponse<BaseApiResponse> =
        uploadDocumentsRequest.run {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val files = ArrayList<MultipartBody.Part>()
            filePaths?.forEach {
                val file = File(it)
                val reqFile: RequestBody =
                    file.asRequestBody(("image/" + file.extension).toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("files", file.name, reqFile)
                files.add(body)
            }

            executeSafely(call = {
                service.uploadDocuments(
                    files,
                    documentType?.toRequestBody(MULTIPART.toMediaTypeOrNull()),
                    dateIssue = dateFormatter.format(dateIssue)
                        .toRequestBody(MULTIPART.toMediaTypeOrNull()),
                    dateExpiry =
                    dateFormatter.format(dateExpiry)
                        .toRequestBody(MULTIPART.toMediaTypeOrNull()),
                    dob =
                    dateFormatter.format(dob)
                        .toRequestBody(MULTIPART.toMediaTypeOrNull()),
                    fullName =
                    fullName?.toRequestBody(MULTIPART.toMediaTypeOrNull()),
                    gender = gender?.toRequestBody(MULTIPART.toMediaTypeOrNull()),
                    identityNo = identityNo?.toRequestBody("multipart/form-dataList".toMediaTypeOrNull()),
                    nationality = nationality?.toRequestBody(MULTIPART.toMediaTypeOrNull())
                )
            })
        }

    override suspend fun getMotherMaidenNames(): ApiResponse<BaseListResponse<String>> =
        executeSafely(call = { service.getMotherMaidenNames() })

    override suspend fun getPlaceOfBirthCities(): ApiResponse<BaseListResponse<String>> =
        executeSafely(call = { service.getCityOfBirthNames() })

    override suspend fun verifySecretQuestions(verifySecretQuestionsRequest: VerifySecretQuestionsRequest): ApiResponse<BaseResponse<Boolean>> =
        executeSafely(call = { service.verifySecretQuestions(verifySecretQuestionsRequest) })

    override suspend fun uploadSelfie(selfiePath: String): ApiResponse<BaseApiResponse> {
        val imageFile = File(selfiePath)
        val reqFile: RequestBody =
            imageFile.asRequestBody(("image/" + imageFile.extension).toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("selfie-picture", imageFile.name, reqFile)
        return executeSafely(call = {
            service.uploadSelfie(body)
        })
    }

    override suspend fun saveCardName(cardName: String): ApiResponse<BaseApiResponse> =
        executeSafely(call = { service.saveCardName(cardName) })

    override suspend fun createNewPasscode(createNewPasscodeRequest: CreateNewPasscodeRequest): ApiResponse<BaseApiResponse> =
        executeSafely(call = { service.createNewPasscode(createNewPasscodeRequest) })

    override suspend fun getCities(): ApiResponse<BaseListResponse<City>> =
        executeSafely(call = { service.getCities() })

    override suspend fun uploadIDCard(file: MultipartBody.Part): ApiResponse<BaseResponse<IDCardDetect>> =
        executeSafely(call = { service.uploadIDCard(file) })

    override suspend fun saveAddressAndOrderCard(cardOrderRequest: CardOrderRequest): ApiResponse<BaseApiResponse> =
        executeSafely(call = { service.saveAddressAndOrderCard(cardOrderRequest) })

    override suspend fun verifyPasscode(passcode: String): ApiResponse<BaseApiResponse> =
        executeSafely(call = {
            service.verifyPasscode(
                VerifyPasscodeRequest(
                    passcode = passcode
                )
            )
        })

    override suspend fun getRecentTransfers(type: String): ApiResponse<BaseListResponse<Beneficiary>> =
        executeSafely(call = { service.getRecentTransfers(type) })

    override suspend fun getReferralAmount(): ApiResponse<BaseResponse<ReferralAmount>> =
        executeSafely(call = { service.getReferralAmount() })

    override suspend fun getY2YUsers(contacts: List<Contact>): ApiResponse<BaseListResponse<Contact>> =
        executeSafely(call = { service.getY2YUsers(contacts) })

    override suspend fun getIBFTBeneficiaries(): ApiResponse<BaseListResponse<BankBeneficiary>> =
        executeSafely(call = { service.getIBFTBeneficiaries() })

    override suspend fun getExternalCards(): ApiResponse<BaseListResponse<ExternalCard>> =
        executeSafely(call = { service.getExternalCards() })

    override suspend fun getAccountBalance(): ApiResponse<BaseResponse<AccountBalance>> =
        executeSafely(call = { service.getAccountBalance() })

    override suspend fun deleteExternalCard(cardId: String): ApiResponse<BaseApiResponse> =
        executeSafely(call = { service.deleteExternalCard(cardId) })

    override suspend fun addExternalCard(
        sessionId: String,
        cardAlias: String,
        cardColor: String,
        cardNumber: String
    ): ApiResponse<BaseResponse<ExternalCard>> =
        executeSafely(call = {
            service.addExternalCard(
                AddExternalCardRequest(
                    alias = cardAlias,
                    color = cardColor,
                    session = CardSession(
                        id = sessionId,
                        number = cardNumber
                    )
                )
            )
        })

    override suspend fun addExternalCardPayment(
        sessionId: String,
        cardAlias: String,
        cardColor: String,
        cardNumber: String
    ): ApiResponse<BaseResponse<ExternalCard>> =
        executeSafely(call = {
            service.addExternalCardPayment(
                AddExternalCardRequest(
                    alias = cardAlias,
                    color = cardColor,
                    session = CardSession(
                        id = sessionId,
                        number = cardNumber
                    )
                )
            )
        })

    override suspend fun getBanksList(): ApiResponse<BaseListResponse<BankData>> =
        executeSafely(call = {
            service.getBanksList()
        })

    override suspend fun findAccountInfo(
        accountNo: String?,
        iban: String?,
        consumerId: String?
    ): ApiResponse<BaseResponse<FindBankAccountInfoResponse>> =
        executeSafely(call = {
            service.findAccountInfo(accountNo = accountNo, iban = iban, consumerId = consumerId)
        })

    override suspend fun deleteIBFTBeneficiary(beneficiaryId: String): ApiResponse<BaseApiResponse> =
        executeSafely(call = { service.deleteIBFTBeneficiary(beneficiaryId) })

    override suspend fun addIBFTBeneficiary(
        title: String?,
        accountNo: String?,
        bankName: String?,
        nickName: String?,
        beneficiaryType: String?
    ): ApiResponse<BaseResponse<BankBeneficiary>> =
        executeSafely(call = {
            val addBeneficiaryRequest = AddBeneficiaryRequest(
                title = title,
                accountNo = accountNo,
                bankName = bankName,
                nickName = nickName,
                beneficiaryType = beneficiaryType
            )
            service.addIBFTBeneficiary(addBeneficiaryRequest = addBeneficiaryRequest)
        })

    override suspend fun updateBankBeneficiary(
        profilePicture: File?,
        nickName: String?,
        id: String?
    ): ApiResponse<BaseResponse<BankBeneficiary>> {
        val reqFile: RequestBody? =
            profilePicture?.asRequestBody(("image/" + profilePicture.extension).toMediaTypeOrNull())
        var body: MultipartBody.Part? = null
        reqFile?.let {
            body =
                MultipartBody.Part.createFormData("file", profilePicture.name, reqFile)
        }
        return executeSafely(call = {
            service.updateBankBeneficiary(
                file = body,
                nickName = nickName?.toRequestBody(MULTIPART.toMediaTypeOrNull()),
                id = id?.toRequestBody(MULTIPART.toMediaTypeOrNull()),
            )
        })
    }

    override suspend fun getUserFromQrCode(uuid: String?): ApiResponse<BaseResponse<QrContact>> =
        executeSafely(call = {
            service.getUserFromQrCode(uuid ?: "")
        })


    companion object {
        const val URL_SEND_VERIFICATION_EMAIL = "/customers/api/sign-up/email"
        const val URL_SIGN_UP = "/customers/api/profile"
        const val URL_POST_DEMOGRAPHIC_DATA = "/customers/api/demographics/"
        const val URL_ACCOUNT_INFO = "/customers/api/accounts"
        const val URL_GET_RANKING = "/customers/api/portal/fetch-ranking"
        const val URL_SEND_INVITE = "/customers/api/save-invite"
        const val URL_SAVE_REFERRAL_INVITATION = "/customers/api/save-referral-invitation"
        const val URL_VERIFY_USER = "/customers/api/verify-user"
        const val URL_POST_DEMOGRAPHIC_DATA_SIGN_IN = "/customers/api/demographics/device-login"
        const val URL_COMPLETE_VERIFICATION = "customers/api/v2/profile"
        const val URL_GET_DOCUMENTS_BY_TYPE = "/customers/api/document-information"
        const val URL_GET_DOCUMENT_DETAILS = "/customers/api/kyc/document-data"
        const val URL_UPLOAD_DOCUMENT = "/customers/api/v2/documents"
        const val URL_GET_MOTHER_NAMES = "/customers/api/getMotherMaidenNames"
        const val URL_GET_PLACE_OF_BIRTH_CITIES = "/customers/api/getCityOfBirthNames"
        const val URL_VERIFY_SECRET_QUESTIONS = "/customers/api/verifySecretQuestions"
        const val URL_UPLOAD_SELFIE = "/customers/api/customers/selfie-picture"
        const val URL_SAVE_CARD_NAME = "/customers/api/accounts/set-card-name"
        const val URL_CREATE_NEW_PASSCODE = "/customers/api/forgot-password"
        const val URL_GET_CITIES = "/customers/api/cities"
        const val URL_SAVE_ADDRESS_ORDERED_CARD = "/cards/api/save-address-and-order-card"
        const val URL_OCR_DETECT = "/digi-ocr/detect/"
        const val URL_VERIFY_PASSCODE = "/customers/api/user/verify-passcode"
        const val URL_GET_RECENT_TRANSFERS = "/customers/api/beneficiaries/recent"
        const val URL_GET_REFERRAL_AMOUNT = "/customers/api/get-referral-amount"
        const val URL_GET_Y2Y_USER = "/customers/api/y2y-beneficiaries"
        const val URL_GET_IBFT_BENEFICIARIES = "/customers/api/beneficiaries/bank-transfer"
        const val URL_GET_EXTERNAL_CARDS = "customers/api/mastercard/beneficiaries"
        const val URL_GET_ACCOUNT_BALANCE = "/customers/api/account/balance"
        const val URL_DELETE_EXTERNAL_CARD = "customers/api/mastercard/beneficiaries/{cardId}"
        const val URL_ADD_EXTERNAL_CARD = "customers/api/mastercard/beneficiaries"
        const val URL_ADD_EXTERNAL_CARD_PAYMENT = "customers/api/external-card/beneficiaries"
        const val URL_GET_BANKS_LIST = "customers/api/bank-detail"
        const val URL_FIND_BANK_ACCOUNT_TITLE = "customers/api/fetch-beneficiary-account-title"
        const val URL_UPDATE_BANK_BENEFICIARY = "customers/api/beneficiaries/bank-transfer"
        const val URL_DELETE_IBFT_BENEFICIARY =
            "customers/api/beneficiaries/bank-transfer/{beneficiaryId}"
        const val URL_ADD_IBFT_BENEFICIARY = "/customers/api/beneficiaries/bank-transfer"
        const val URL_GET_QR_CODE_USER = "/customers/api/customers-info/{uuid}"

    }

}
