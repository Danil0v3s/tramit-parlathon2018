package br.com.firstsoft.parlathon.util


/**
 * Created by danilolemes on 06/01/2018.
 */
//class ShareHelper(private val mContext: Context) {
//    var shareBottomSheetDialog: ShareBottomSheetDialog? = null
//
//    fun shareImageWithCaption(caption: String, uri: Uri, bookUrl: String) {
//        val sendIntent = Intent(Intent.ACTION_SEND)
//        sendIntent.type = "image/jpeg"
//        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Compartilhar")
//        sendIntent.putExtra(Intent.EXTRA_TEXT, caption)
//        sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
//        val availableApps = mContext.packageManager.queryIntentActivities(sendIntent, 0).distinctBy { it.activityInfo.applicationInfo.packageName }
//        shareBottomSheetDialog = ShareBottomSheetDialog(availableApps, mContext, shareListener(sendIntent, caption, bookUrl))
//        shareBottomSheetDialog?.show((mContext as AppCompatActivity).supportFragmentManager, "SHARE_DIALOG")
//
//    }
//
//    private fun shareListener(sendIntent: Intent, caption: String, bookUrl: String): (ResolveInfo) -> Unit {
//        return { resolveInfo ->
//            shareBottomSheetDialog?.dismiss()
//            if (!resolveInfo.activityInfo.applicationInfo.packageName.contains("facebook")) {
//                sendIntent.setClassName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name)
//                mContext.startActivity(sendIntent)
//            } else if (resolveInfo.activityInfo.applicationInfo.packageName.equals("com.facebook.orca")) {
//                val messengerIntent = Intent(Intent.ACTION_SEND)
//                messengerIntent.type = "text/plain"
//                messengerIntent.putExtra(Intent.EXTRA_SUBJECT, "Compartilhar")
//                messengerIntent.putExtra(Intent.EXTRA_TEXT, caption)
//                messengerIntent.setClassName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name)
//                mContext.startActivity(messengerIntent)
//                //                val content = ShareLinkContent.Builder()
//                //                        .setContentUrl(Uri.parse(bookUrl))
//                //                        .build()
//                //
//                //                MessageDialog(mContext as Activity).show(content)
//            } else {
//                val content = ShareLinkContent.Builder()
//                        .setContentUrl(Uri.parse(bookUrl))
//                        .setQuote(caption)
//                        .setShareHashtag(ShareHashtag.Builder()
//                                .setHashtag("#Quixote")
//                                .build())
//                        .build()
//
//                ShareDialog(mContext as Activity).show(content, ShareDialog.Mode.AUTOMATIC)
//            }
//        }
//    }
//
//    @SuppressLint("ValidFragment")
//    class ShareBottomSheetDialog() : BottomSheetDialogFragment() {
//
//        private var listener: (ResolveInfo) -> Unit = {}
//        private var availableApps = listOf<ResolveInfo>()
//        private var mContext: Context? = null
//
//        constructor(availableApps: List<ResolveInfo>, context: Context, listener: (ResolveInfo) -> Unit) : this() {
//            this.listener = listener
//            this.availableApps = availableApps
//            this.mContext = context
//        }
//
//        @SuppressLint("RestrictedApi")
//        override fun setupDialog(dialog: Dialog, style: Int) {
//            super.setupDialog(dialog, style)
//            val contentView = View.inflate(context, R.layout.bottomsheet_share, null)
//            dialog.setContentView(contentView)
//            val shareSheetAdapter = ShareSheetAdapter(availableApps, mContext!!, listener)
//            contentView.sheetShareRecycler.adapter = shareSheetAdapter
//            contentView.sheetShareRecycler.layoutManager = GridLayoutManager(context, 3)
//
//            val layoutParams = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
//            val behavior = layoutParams.behavior
//            if (behavior != null && behavior is BottomSheetBehavior<*>) {
//                behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//
//                    override fun onStateChanged(bottomSheet: View, newState: Int) {
//                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                            dismiss()
//                        }
//                    }
//
//                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
//                })
//            }
//        }
//
//    }
//
//    class ShareSheetAdapter(private val list: List<ResolveInfo>, private val context: Context, private val listener: (ResolveInfo) -> Unit) : RecyclerView.Adapter<ShareSheetAdapter.ShareSheetViewHolder>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ShareSheetViewHolder(LayoutInflater.from(this.context).inflate(R.layout.holder_package_item, parent, false))
//
//        override fun onBindViewHolder(holder: ShareSheetViewHolder, position: Int) = holder.bind(list[position])
//
//        override fun getItemCount(): Int = list.size
//
//        inner class ShareSheetViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
//
//            private val packageIcon = view.packageIcon
//            private val packageName = view.packageName
//            private var resolveInfo: ResolveInfo? = null
//
//            init {
//                view.setOnClickListener(this)
//            }
//
//            fun bind(resolveInfo: ResolveInfo) {
//                this.resolveInfo = resolveInfo
//                this.packageIcon.setImageDrawable(resolveInfo.activityInfo.applicationInfo.loadIcon(context.packageManager))
//                this.packageName.text = resolveInfo.activityInfo.applicationInfo.loadLabel(context.packageManager).toString()
//            }
//
//            override fun onClick(p0: View?) {
//                resolveInfo?.let { listener(it) }
//            }
//
//        }
//
//    }
//}