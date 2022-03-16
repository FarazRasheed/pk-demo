package com.yap.yappk.networking.microservices.customers

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
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface CustomersRetroService {

    // In onboarding send verification email to verify user
    @POST(CustomersRepository.URL_SEND_VERIFICATION_EMAIL)
    suspend fun emailVerification(@Body emailVerificationRequest: EmailVerificationRequest): Response<BaseResponse<String>>

    // Save user profile
    @POST(CustomersRepository.URL_SIGN_UP)
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<BaseResponse<String>>

    // Post demographic dataList
    @PUT(CustomersRepository.URL_POST_DEMOGRAPHIC_DATA)
    suspend fun postDemographicData(@Body demographicDataRequest: DemographicDataRequest): Response<BaseApiResponse>

    // Get user account(s) Info
    @GET(CustomersRepository.URL_ACCOUNT_INFO)
    suspend fun getAccountInfo(): Response<BaseListResponse<AccountInfo>>

    // Get user waiting list ranking
    @GET(CustomersRepository.URL_GET_RANKING)
    suspend fun getWaitingRanking(): Response<BaseResponse<WaitingListResponse>>

    // Invite a friend
    @POST(CustomersRepository.URL_SEND_INVITE)
    suspend fun sendInviteFriend(@Body sendInviteFriendRequest: SendInviteFriendRequest): Response<BaseApiResponse>

    // Save Referral
    @POST(CustomersRepository.URL_SAVE_REFERRAL_INVITATION)
    suspend fun saveReferralInvitation(@Body saveReferralRequest: SaveReferralRequest): Response<BaseApiResponse>

    // Verify user
    @POST(CustomersRepository.URL_VERIFY_USER)
    suspend fun verifyUser(@Query("username") username: String): Response<BaseResponse<Boolean>>

    // Send Otp for device validate
    @POST(CustomersRepository.URL_POST_DEMOGRAPHIC_DATA_SIGN_IN)
    suspend fun generateOTPForDeviceVerification(@Body demographicDataRequest: DemographicDataRequest): Response<BaseResponse<BaseApiResponse>>

    // Verify OTP for device validate
    @PUT(CustomersRepository.URL_POST_DEMOGRAPHIC_DATA_SIGN_IN)
    suspend fun verifyOTPForDeviceVerification(@Body demographicDataRequest: DemographicDataRequest): Response<BaseResponse<String>>

    // Assign IBAN for complete verification
    @POST(CustomersRepository.URL_COMPLETE_VERIFICATION)
    suspend fun completeVerification(@Body completeVerificationRequest: CompleteVerificationRequest): Response<BaseApiResponse>

    // Get Documents by type
    @GET(CustomersRepository.URL_GET_DOCUMENTS_BY_TYPE)
    suspend fun getDocumentsByType(@Query("documentType") documentType: String): Response<BaseResponse<Documents>>

    // Get Documents Details
    @POST(CustomersRepository.URL_GET_DOCUMENT_DETAILS)
    suspend fun getDocumentDetails(@Body documentDetailRequest: DocumentDetailRequest): Response<BaseResponse<DocumentDetails>>

    @Multipart
    @POST(CustomersRepository.URL_UPLOAD_DOCUMENT)
    suspend fun uploadDocuments(
        @Part files: List<MultipartBody.Part>,
        @Part("documentType") documentType: RequestBody?,
        @Part("dateIssue") dateIssue: RequestBody?,
        @Part("dateExpiry") dateExpiry: RequestBody?,
        @Part("dob") dob: RequestBody?,
        @Part("fullName") fullName: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part("identityNo") identityNo: RequestBody?,
        @Part("nationality") nationality: RequestBody?
    ): Response<BaseApiResponse>

    // Get Secret questions mother name
    @GET(CustomersRepository.URL_GET_MOTHER_NAMES)
    suspend fun getMotherMaidenNames(): Response<BaseListResponse<String>>

    // Get Secret questions birth city name
    @GET(CustomersRepository.URL_GET_PLACE_OF_BIRTH_CITIES)
    suspend fun getCityOfBirthNames(): Response<BaseListResponse<String>>

    // verify Secret Questions answers
    @POST(CustomersRepository.URL_VERIFY_SECRET_QUESTIONS)
    suspend fun verifySecretQuestions(@Body verifySecretQuestionsRequest: VerifySecretQuestionsRequest): Response<BaseResponse<Boolean>>

    // upload selfie
    @Multipart
    @POST(CustomersRepository.URL_UPLOAD_SELFIE)
    suspend fun uploadSelfie(
        @Part file: MultipartBody.Part,
    ): Response<BaseApiResponse>

    // save card name
    @POST(CustomersRepository.URL_SAVE_CARD_NAME)
    suspend fun saveCardName(@Query("cardName") cardName: String): Response<BaseApiResponse>

    //create new passcode
    @PUT(CustomersRepository.URL_CREATE_NEW_PASSCODE)
    suspend fun createNewPasscode(@Body createNewPasscodeRequest: CreateNewPasscodeRequest): Response<BaseApiResponse>

    // Get cities for user address
    @GET(CustomersRepository.URL_GET_CITIES)
    suspend fun getCities(): Response<BaseListResponse<City>>

    // Save user address and order card
    @POST(CustomersRepository.URL_SAVE_ADDRESS_ORDERED_CARD)
    suspend fun saveAddressAndOrderCard(@Body cardOrderRequest: CardOrderRequest): Response<BaseApiResponse>

    // upload ID card
    @Multipart
    @POST(CustomersRepository.URL_OCR_DETECT)
    suspend fun uploadIDCard(
        @Part file: MultipartBody.Part,
    ): Response<BaseResponse<IDCardDetect>>

    // Verify passcode
    @POST(CustomersRepository.URL_VERIFY_PASSCODE)
    suspend fun verifyPasscode(@Body verifyPasscodeRequest: VerifyPasscodeRequest): Response<BaseApiResponse>

    // Recent transfers
    @GET(CustomersRepository.URL_GET_RECENT_TRANSFERS)
    suspend fun getRecentTransfers(@Query("type") recentType: String): Response<BaseListResponse<Beneficiary>>

    // Get referral amount
    @GET(CustomersRepository.URL_GET_REFERRAL_AMOUNT)
    suspend fun getReferralAmount(): Response<BaseResponse<ReferralAmount>>

    @POST(CustomersRepository.URL_GET_Y2Y_USER)
    suspend fun getY2YUsers(@Body contacts: List<Contact>): Response<BaseListResponse<Contact>>

    @GET(CustomersRepository.URL_GET_IBFT_BENEFICIARIES)
    suspend fun getIBFTBeneficiaries(): Response<BaseListResponse<BankBeneficiary>>

    @GET(CustomersRepository.URL_GET_EXTERNAL_CARDS)
    suspend fun getExternalCards(): Response<BaseListResponse<ExternalCard>>

    @GET(CustomersRepository.URL_GET_ACCOUNT_BALANCE)
    suspend fun getAccountBalance(): Response<BaseResponse<AccountBalance>>

    @DELETE(CustomersRepository.URL_DELETE_EXTERNAL_CARD)
    suspend fun deleteExternalCard(@Path("cardId") cardId: String): Response<BaseApiResponse>

    @POST(CustomersRepository.URL_ADD_EXTERNAL_CARD)
    suspend fun addExternalCard(@Body addExternalCardRequest: AddExternalCardRequest): Response<BaseResponse<ExternalCard>>

    @POST(CustomersRepository.URL_ADD_EXTERNAL_CARD_PAYMENT)
    suspend fun addExternalCardPayment(@Body addExternalCardRequest: AddExternalCardRequest): Response<BaseResponse<ExternalCard>>

    @GET(CustomersRepository.URL_GET_BANKS_LIST)
    suspend fun getBanksList(): Response<BaseListResponse<BankData>>

    @GET(CustomersRepository.URL_FIND_BANK_ACCOUNT_TITLE)
    suspend fun findAccountInfo(
        @Query("accountNo") accountNo: String?,
        @Query("iban") iban: String?,
        @Query("consumerId") consumerId: String?
    ): Response<BaseResponse<FindBankAccountInfoResponse>>

    @DELETE(CustomersRepository.URL_DELETE_IBFT_BENEFICIARY)
    suspend fun deleteIBFTBeneficiary(@Path("beneficiaryId") beneficiaryId: String): Response<BaseApiResponse>

    @POST(CustomersRepository.URL_ADD_IBFT_BENEFICIARY)
    suspend fun addIBFTBeneficiary(@Body addBeneficiaryRequest: AddBeneficiaryRequest): Response<BaseResponse<BankBeneficiary>>

    // Beneficiary Update
    @Multipart
    @PUT(CustomersRepository.URL_UPDATE_BANK_BENEFICIARY)
    suspend fun updateBankBeneficiary(
        @Part file: MultipartBody.Part?,
        @Part("nickName") nickName: RequestBody?,
        @Part("id") id: RequestBody?,
    ): Response<BaseResponse<BankBeneficiary>>

    @GET(CustomersRepository.URL_GET_QR_CODE_USER)
    suspend fun getUserFromQrCode(@Path("uuid") uuid: String): Response<BaseResponse<QrContact>>

}
