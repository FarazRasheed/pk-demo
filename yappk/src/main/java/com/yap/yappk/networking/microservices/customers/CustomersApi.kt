package com.yap.yappk.networking.microservices.customers

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.authentication.requestdtos.DemographicDataRequest
import com.yap.yappk.networking.microservices.customers.requestsdtos.*
import com.yap.yappk.networking.microservices.customers.responsedtos.*
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.networking.microservices.customers.responsedtos.documents.Documents
import com.yap.yappk.networking.microservices.customers.responsedtos.documents.IDCardDetect
import okhttp3.MultipartBody
import java.io.File

interface CustomersApi {
    suspend fun emailVerification(emailVerificationRequest: EmailVerificationRequest): ApiResponse<BaseResponse<String>>
    suspend fun signUp(signUpRequest: SignUpRequest): ApiResponse<BaseResponse<String>>
    suspend fun postDemographicData(demographicDataRequest: DemographicDataRequest): ApiResponse<BaseApiResponse>
    suspend fun getAccountInfo(): ApiResponse<BaseListResponse<AccountInfo>>
    suspend fun getWaitingRanking(): ApiResponse<BaseResponse<WaitingListResponse>>
    suspend fun saveReferralInvitation(saveReferralRequest: SaveReferralRequest): ApiResponse<BaseApiResponse>
    suspend fun sendInviteFriend(sendInviteFriendRequest: SendInviteFriendRequest): ApiResponse<BaseApiResponse>
    suspend fun verifyUser(user: String): ApiResponse<BaseResponse<Boolean>>
    suspend fun generateOTPForDeviceVerification(demographicDataRequest: DemographicDataRequest): ApiResponse<BaseApiResponse>
    suspend fun verifyOTPForDeviceVerification(demographicDataRequest: DemographicDataRequest): ApiResponse<BaseResponse<String>>
    suspend fun completeVerification(completeVerificationRequest: CompleteVerificationRequest): ApiResponse<BaseApiResponse>
    suspend fun getDocumentsByType(documentType: String): ApiResponse<BaseResponse<Documents>>
    suspend fun getDocumentDetail(documentDetailRequest: DocumentDetailRequest): ApiResponse<BaseResponse<DocumentDetails>>
    suspend fun uploadDocument(uploadDocumentsRequest: UploadDocumentsRequest): ApiResponse<BaseApiResponse>
    suspend fun getMotherMaidenNames(): ApiResponse<BaseListResponse<String>>
    suspend fun getPlaceOfBirthCities(): ApiResponse<BaseListResponse<String>>
    suspend fun verifySecretQuestions(verifySecretQuestionsRequest: VerifySecretQuestionsRequest): ApiResponse<BaseResponse<Boolean>>
    suspend fun uploadSelfie(selfiePath: String): ApiResponse<BaseApiResponse>
    suspend fun saveCardName(cardName: String): ApiResponse<BaseApiResponse>
    suspend fun createNewPasscode(createNewPasscodeRequest: CreateNewPasscodeRequest): ApiResponse<BaseApiResponse>
    suspend fun getCities(): ApiResponse<BaseListResponse<City>>
    suspend fun uploadIDCard(file: MultipartBody.Part): ApiResponse<BaseResponse<IDCardDetect>>
    suspend fun saveAddressAndOrderCard(cardOrderRequest: CardOrderRequest): ApiResponse<BaseApiResponse>
    suspend fun verifyPasscode(passcode: String): ApiResponse<BaseApiResponse>
    suspend fun getRecentTransfers(type: String): ApiResponse<BaseListResponse<Beneficiary>>
    suspend fun getReferralAmount(): ApiResponse<BaseResponse<ReferralAmount>>
    suspend fun getY2YUsers(contacts: List<Contact>): ApiResponse<BaseListResponse<Contact>>
    suspend fun getIBFTBeneficiaries(): ApiResponse<BaseListResponse<BankBeneficiary>>
    suspend fun getExternalCards(): ApiResponse<BaseListResponse<ExternalCard>>
    suspend fun getAccountBalance(): ApiResponse<BaseResponse<AccountBalance>>
    suspend fun deleteExternalCard(cardId: String): ApiResponse<BaseApiResponse>
    suspend fun addExternalCard(
        sessionId: String,
        cardAlias: String,
        cardColor: String,
        cardNumber: String
    ): ApiResponse<BaseResponse<ExternalCard>>

    suspend fun addExternalCardPayment(
        sessionId: String,
        cardAlias: String,
        cardColor: String,
        cardNumber: String
    ): ApiResponse<BaseResponse<ExternalCard>>

    suspend fun getBanksList(): ApiResponse<BaseListResponse<BankData>>

    suspend fun findAccountInfo(
        accountNo: String?,
        iban: String?,
        consumerId: String?
    ): ApiResponse<BaseResponse<FindBankAccountInfoResponse>>

    suspend fun deleteIBFTBeneficiary(beneficiaryId: String): ApiResponse<BaseApiResponse>

    suspend fun addIBFTBeneficiary(
        title: String?,
        accountNo: String?,
        bankName: String?, nickName: String?, beneficiaryType: String?
    ): ApiResponse<BaseResponse<BankBeneficiary>>
    suspend fun updateBankBeneficiary(
        profilePicture: File?,
        nickName: String?,
        id: String?,
    ): ApiResponse<BaseResponse<BankBeneficiary>>

    suspend fun getUserFromQrCode(
        uuid: String?,
    ): ApiResponse<BaseResponse<QrContact>>

}
